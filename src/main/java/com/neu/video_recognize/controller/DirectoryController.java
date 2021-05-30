package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.Directory;
import com.neu.video_recognize.entity.vo.DeleteFileListWrapper;
import com.neu.video_recognize.service.directory.DirectoryService;
import com.neu.video_recognize.service.file.FileService;
import com.neu.video_recognize.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/directory")
public class DirectoryController {

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @RequestMapping("/getFilesAndDirectories")
    public Map<String, Object> getFilesAndDirectory(@RequestParam("parentId") Integer parentId, HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        Map<String, Object> rs = directoryService.getFilesAndDirectoryBy(parentId, uId);
        rs.put("user", userService.selectUserByPrimaryKey(uId));
        rs.put("status", 1);
        return rs;
    }

    @RequestMapping("getDirectories")
    public Map<String, Object> getDirectories(@RequestParam("parentId") Integer parentId, HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        List<Directory> directories = directoryService.getDirectories(parentId, uId);
        Map<String, Object> rs = new HashMap<>();
        rs.put("directories", directories);
        rs.put("status", 1);
        return rs;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addDirectory(HttpSession session, @RequestBody Directory directory){
        Integer uId = (Integer) session.getAttribute("userId");
        directory.setOwnerId(uId);
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
        Integer uId = (Integer) session.getAttribute("userId");
        int status = directoryService.deleteByPrimaryKey(directoryId, uId);
        return Collections.singletonMap("status", status);
    }

    @PostMapping("/deleteAll")
    public Map<String, Object> deleteAll(@RequestBody DeleteFileListWrapper wrapper, HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        for (Integer fId : wrapper.getFileIds())
            fileService.deleteByPrimaryKey(fId, uId);

        for (Integer dId: wrapper.getDirectoryIds())
            directoryService.deleteByPrimaryKey(dId, uId);
        return Collections.singletonMap("status", 1);
    }

    @RequestMapping("/moveFile")
    public Map<String, Object> moveFile(@RequestParam("fileId") Integer fileId,
                                        @RequestParam("newParentId") Integer newParentId,
                                        @RequestParam("isDirectory") Boolean isDirectory){
        Map<String, Object> rs = new HashMap<>();
        int status = directoryService.moveFile(fileId, newParentId, isDirectory, rs);
        rs.put("status", status);
        return rs;
    }
}
