package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.Token;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.RecognizeInfo;
import com.neu.video_recognize.entity.vo.VideoInfo;
import com.neu.video_recognize.service.file.FileService;
import com.neu.video_recognize.service.invoke.InvokeService;
import com.neu.video_recognize.service.token.TokenService;
import com.neu.video_recognize.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invoke")
public class InvokeController {

    @Autowired
    private InvokeService invokeService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @RequestMapping("/recognize")
    public Map<String, Object> recognize(@RequestParam("fileId") Integer fileId, HttpSession session) throws IOException {
        Integer uId = (Integer) session.getAttribute("userId");
        Map<String, Object> rs = new HashMap<>(5);
        if (invokeService.requestInvokePermission(uId)){
            invokeService.invokeAlgorithm(fileId);
            int flag = invokeService.insertRecord(uId, null);
            rs.put("status", flag);
        }else{
            rs.put("status", 0);
            rs.put("msg", "调用次数已经耗尽");
        }
        return rs;
    }

    @RequestMapping("/token/recognize")
    public Map<String, Object> recognizeWithToken(RecognizeInfo info) throws IOException {
        Map<String, Object> rs = new HashMap<>(Collections.singletonMap("status", 0));

        User u = verifyTokenAndGetUser(info.getToken(), rs);
        if (u == null){
            return rs;
        }

        if (!invokeService.requestInvokePermission(u.getId())){
            rs.put("msg", "调用次数已经耗尽");
            return rs;
        }

        File f = fileService.saveVideo(new VideoInfo(info.getVideo(), null, -1, u.getId()));
        invokeService.invokeAlgorithm(f.getId());
        invokeService.insertRecord(u.getId(), info.getToken());
        rs.put("videoId", f.getId());
        rs.put("msg", "成功调用！");
        rs.put("status", 1);
        return rs;
    }

    @RequestMapping("/token/result")
    public Map<String, Object> getResult(@RequestParam("token") String token, @RequestParam("videoId") Integer fileId){
        Map<String, Object> rs = new HashMap<>(Collections.singletonMap("status", 0));
        User u = verifyTokenAndGetUser(token, rs);
        if (u == null)
            return rs;
        File f = fileService.getFileByPrimaryKey(fileId);
        if (f == null){
            rs.put("msg", "文件不存在!");
        }else if (!f.getOwnerId().equals(u.getId())){
            rs.put("msg", "文件不属于您，您无权查看此文件的信息！");
        }else{
            rs.put("result", f.getRecognizeResult());
            rs.put("lastRecognizeTime", f.getLastRecognizeTime());
            rs.put("status", 1);
        }
        return rs;
    }

    @RequestMapping("/recordsAndUser")
    public Map<String, Object> getRecords(HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        Map<String, Object> rs = new HashMap<>();
        rs.put("records", invokeService.getInvokeRecordsBy(uId));
        rs.put("user", userService.selectUserByPrimaryKey(uId));
        rs.put("status", 1);
        return rs;
    }

    // 验证token是否有效，如果有效，会返回与token相对应的user对象
    private User verifyTokenAndGetUser(String token, Map<String, Object> rs){
        Integer userId = tokenService.getClaimedUserId(token);
        if (userId == null){
            rs.put("msg", "错误或不符合要求的token！");
            return null;
        }

        User u = userService.selectUserByPrimaryKey(userId);
        if (u == null){
            rs.put("msg", "找不到与token相对应的的用户信息");
            return null;
        }

        boolean valid = tokenService.verify(token, u.getSecretKey());
        if (!valid){
            rs.put("msg", "无效的token");
            return null;
        }
        return u;
    }
}
