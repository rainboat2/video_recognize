package com.neu.video_recognize.controller;

import com.neu.video_recognize.service.invoke.InvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invoke")
public class InvokeController {

    @Autowired
    private InvokeService invokeService;

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
}
