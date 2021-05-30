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
        File info = new File();
        info.setParentId(directoryId);
        info.setOwnerId(userId);
        List<File> files = fileMapper.selectByOwnerAndParent(info);
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
    public int deleteByPrimaryKey(Integer directoryId, Integer userId) {
        // 获取文件夹
        Directory d = directoryMapper.selectByPrimaryKey(directoryId);

        // 构建查询数据时用的对象
        Directory dInfo = new Directory();
        File fInfo = new File();
        dInfo.setOwnerId(userId);
        fInfo.setOwnerId(userId);

        // 广度优先遍历，找到该文件夹下的所有文件和文件夹
        Queue<Directory> directoryQueue = new LinkedList<>();
        List<File> files = new ArrayList<>();
        List<Directory> directories = new ArrayList<>();
        directories.add(d);
        directoryQueue.add(d);
        while (!directoryQueue.isEmpty()){
            Directory cur = directoryQueue.poll();
            // 当前文件夹下的所有文件夹和文件
            dInfo.setParentId(cur.getId());
            List<Directory> dirs = directoryMapper.selectByOwnerAndParent(dInfo);
            fInfo.setParentId(cur.getId());
            List<File> fis = fileMapper.selectByOwnerAndParent(fInfo);

            // 将文件夹加入到队列
            directoryQueue.addAll(dirs);
            // 待删除列表
            directories.addAll(dirs);
            files.addAll(fis);
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
        User u = userMapper.selectByPrimaryKey(userId);
        User updateInfo = new User();
        updateInfo.setId(u.getId());
        updateInfo.setOwnFileSize(u.getOwnFileSize() - totalSize);
        userMapper.updateByPrimaryKeySelective(updateInfo);

        return 1;
    }

    @Override
    public int moveFile(Integer fileId, Integer newParentId, Boolean isDirectory, Map<String, Object> rs) {
        // 将文件或文件夹移动到新的文件夹下面
        if (isDirectory){
            if (hasCycle(fileId, newParentId)){
                rs.put("msg", "文件不能移动到其自身或其子目录下");
                return 0;
            }else{
                renameIfSame(fileId, newParentId, true);
                Directory d = new Directory();
                d.setId(fileId);
                d.setParentId(newParentId);
                return directoryMapper.updateByPrimaryKeySelective(d);
            }
        }else{
            renameIfSame(fileId, newParentId, false);
            File f = new File();
            f.setId(fileId);
            f.setParentId(newParentId);
            return fileMapper.updateByPrimaryKeySelective(f);
        }
    }

    private boolean hasCycle(Integer directoryId, Integer newParentId){
        // 要想不成环，就要求newParentId不是directoryId本身或是其子目录
        Integer cur = newParentId;
        boolean flag = false;
        while (cur != -1){
            if (cur.equals(directoryId)){
                flag = true;
                break;
            }
            cur = directoryMapper.selectByPrimaryKey(cur).getParentId();
        }
        return flag;
    }

    @Override
    public String renameIfSame(Integer fileId, Integer newParentId, boolean isDirectory){
        String newName;
        Set<String> names;
        if (isDirectory){
            List<Directory> directories = directoryMapper.selectByParentId(newParentId);
            // 提取所有文件夹的名字
            names = new HashSet<>(directories.size() * 2);
            for (Directory di : directories)
                names.add(di.getName());
            Directory d = directoryMapper.selectByPrimaryKey(fileId);
            newName = renameIfSame(d.getName(), names, true);
            // 如果名称被更改了，那么将其更新到数据库
            if (!newName.equals(d.getName())){
                Directory updateInfo = new Directory();
                updateInfo.setId(d.getId());
                updateInfo.setName(newName);
                directoryMapper.updateByPrimaryKeySelective(updateInfo);
            }
        }else{
            List<File> files = fileMapper.selectByParentId(newParentId);
            names = new HashSet<>(files.size() * 2);
            for (File fi : files)
                names.add(fi.getName());
            File f = fileMapper.selectByPrimaryKey(fileId);
            newName = renameIfSame(f.getName(), names, false);
            if (!newName.equals(f.getName())){
                File update = new File();
                update.setId(fileId);
                update.setName(newName);
                fileMapper.updateByPrimaryKeySelective(update);
            }
        }
        return newName;
    }

    private String renameIfSame(String name, Set<String> names, boolean isDirectory){
        String prefix = name, suffix = "";
        if (!isDirectory){
            int dot = name.lastIndexOf('.');
            prefix = name.substring(0, dot);
            suffix = name.substring(dot);
        }
        int cnt = 1;
        String newName = prefix + suffix;
        while (names.contains(newName)){
            newName = prefix + "(" + cnt + ")" + suffix;
            cnt++;
        }
        return newName;
    }
}
