package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.vo.VideoInfo;
import com.neu.video_recognize.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/getById")
    public Map<String, Object> getById(@RequestParam("id") Integer id){
        return Collections.singletonMap("file", fileService.getFileByPrimaryKey(id));
    }

    @RequestMapping("/uploadVideo")
    public Map<String, Object> upload(VideoInfo fi, HttpSession session) throws IOException {
        // 设置文件的所有者
        Integer uId = (Integer) session.getAttribute("userId");
        fi.setOwnerId(uId);
        // 存储视频
        File f = fileService.saveVideo(fi);
        Map<String, Object> rs = new HashMap<>(5);
        rs.put("file", f);
        rs.put("status", 1);
        return rs;
    }

    @RequestMapping("/rename")
    public Map<String, Object> rename(@RequestParam("id") Integer fileId, @RequestParam("name") String name){
        int status = fileService.rename(fileId, name);
        return Collections.singletonMap("status", status);
    }

    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Integer fileId, HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        int status = fileService.deleteByPrimaryKey(fileId, uId);
        return Collections.singletonMap("status", status);
    }
}
