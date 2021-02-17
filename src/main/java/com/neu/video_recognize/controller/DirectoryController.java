package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.Directory;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.service.directory.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/directory")
public class DirectoryController {

    @Autowired
    private DirectoryService directoryService;

    @RequestMapping("/getFilesAndDirectories")
    public Map<String, Object> getFilesAndDirectory(@RequestParam("parentId") Integer parentId, HttpSession session){
        User u = (User) session.getAttribute("user");
        Map<String, Object> rs = directoryService.getFilesAndDirectoryBy(parentId, u.getId());
        rs.put("status", 1);
        return rs;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addDirectory(HttpSession session, @RequestBody Directory directory){
        User u = (User) session.getAttribute("user");
        directory.setOwnerId(u.getId());
        int status = directoryService.addDirectory(directory);
        return Collections.singletonMap("status", status);
    }

    @RequestMapping("/rename")
    public Map<String, Object> rename(@RequestParam("id") Integer directoryId, @RequestParam("name") String name){
        int status = directoryService.rename(directoryId, name);
        return Collections.singletonMap("status", status);
    }

    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Integer directoryId, HttpSession session){
        User u = (User) session.getAttribute("user");
        int status = directoryService.deleteByPrimaryKey(directoryId, u);
        return Collections.singletonMap("status", status);
    }
}
