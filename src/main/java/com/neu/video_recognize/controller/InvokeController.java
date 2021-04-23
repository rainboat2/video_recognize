package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.service.invoke.InvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/invoke")
public class InvokeController {

    @Autowired
    private InvokeService invokeService;

    @RequestMapping("/recognize")
    public Map<String, Object> recognize(@RequestParam("fileId") Integer fileId, HttpSession session) throws IOException {
        Integer uId = (Integer) session.getAttribute("userId");

        invokeService.invokeAlgorithm(fileId);
        invokeService.insertRecord(uId, null);
        return Collections.singletonMap("status", 1);
    }
}
