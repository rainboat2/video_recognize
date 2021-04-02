package com.neu.video_recognize.service.directory;

import com.neu.video_recognize.entity.po.Directory;
import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.mapper.DirectoryMapper;
import com.neu.video_recognize.mapper.FileMapper;
import com.neu.video_recognize.mapper.FileSaver;
import com.neu.video_recognize.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DirectoryServiceImpl implements DirectoryService{

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DirectoryMapper directoryMapper;

    @Autowired
    private FileSaver fileSaver;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getFilesAndDirectoryBy(Integer directoryId, Integer userId) {
        List<File> files = fileMapper.selectByOwnerAndParent(new File(userId, directoryId));
        List<Directory> directories = directoryMapper.selectByOwnerAndParent(new Directory(userId, directoryId));

        Map<String, Object> rs = new HashMap<>();
        rs.put("files", files);
        rs.put("directories", directories);
        return rs;
    }

    @Override
    public List<Directory> getDirectories(Integer directoryId, Integer userId) {
        return directoryMapper.selectByOwnerAndParent(new Directory(userId, directoryId));
    }

    @Override
    public int addDirectory(Directory d) {
        return directoryMapper.insert(d);
    }

    @Override
    public int rename(Integer directoryId, String name) {
        Directory d = new Directory();
        d.setId(directoryId);
        d.setName(name);
        return directoryMapper.updateByPrimaryKeySelective(d);
    }

    @Override
    public int deleteByPrimaryKey(Integer directoryId, User u) {
        // 获取文件夹
        Directory d = directoryMapper.selectByPrimaryKey(directoryId);

        // 构建查询数据时用的数据
        Directory dInfo = new Directory();
        File fInfo = new File();
        dInfo.setOwnerId(u.getId());
        fInfo.setOwnerId(u.getId());

        // 广度优先遍历，找到该文件夹下的所有文件和文件夹
        Queue<Directory> directoryQueue = new LinkedList<>();
        List<File> files = new ArrayList<>();
        List<Directory> directories = new ArrayList<>();
        directories.add(d);
        directoryQueue.add(d);
        while (!directoryQueue.isEmpty()){
            Directory cur = directoryQueue.poll();
            // 当前文件夹下的所有文件夹
            dInfo.setParentId(cur.getId());
            directoryQueue.addAll(directoryMapper.selectByOwnerAndParent(dInfo));

            // 当前文件夹下的所有文件
            fInfo.setParentId(cur.getId());
            files.addAll(fileMapper.selectByOwnerAndParent(fInfo));
        }

        // 依次删除找到的所有文件，并记录被删除文件的大小
        int totalSize = 0;
        for (File f : files){
            totalSize += f.getSize();
            fileSaver.deleteVideo(f.getFilePath());
            fileSaver.deleteImage(f.getCoverImagePath());
        }
        // 批量删除文件记录
        for (File f : files){
            fileMapper.deleteByPrimaryKey(f.getId());
        }

        // 批量删除所有文件夹记录
        for (Directory dir : directories)
            directoryMapper.deleteByPrimaryKey(dir.getId());

        // 更新用户拥有的文件大小信息
        u.setOwnFileSize(u.getOwnFileSize() - totalSize);
        User updateInfo = new User();
        updateInfo.setId(u.getId());
        updateInfo.setOwnFileSize(u.getOwnFileSize());
        userMapper.updateByPrimaryKeySelective(updateInfo);

        return 1;
    }
}
