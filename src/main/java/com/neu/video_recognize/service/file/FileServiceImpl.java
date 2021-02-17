package com.neu.video_recognize.service.file;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.VideoInfo;
import com.neu.video_recognize.mapper.FileMapper;
import com.neu.video_recognize.mapper.FileSaver;
import com.neu.video_recognize.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileSaver fileSaver;

    @Autowired
    private UserMapper userMapper;

    @Override
    public File getFileByPrimaryKey(Integer id) {
        return fileMapper.selectByPrimaryKey(id);
    }

    @Override
    public File saveVideo(VideoInfo fi, User u) throws IOException {
        // 存储文件到磁盘
        String filePathName = fileSaver.saveVideo(fi.getVideo());
        // 保存相关记录到数据库
        File file = new File(fi);
        file.setFilePath(filePathName);
        fileMapper.insert(file);

        // 更新用户的存储空间使用量
        u.setOwnFileSize(u.getOwnFileSize() + (int) fi.getVideo().getSize());
        User updateInfo = new User();
        updateInfo.setOwnFileSize(u.getOwnFileSize());
        updateInfo.setId(u.getId());
        userMapper.updateByPrimaryKeySelective(updateInfo);

        return file;
    }

    @Override
    public int rename(Integer fileId, String name) {
        File f = new File();
        f.setId(fileId);
        f.setName(name);
        return fileMapper.updateByPrimaryKeySelective(f);
    }

    @Override
    public int deleteByPrimaryKey(Integer id, User u) {
        File f = fileMapper.selectByPrimaryKey(id);

        // 更新用户拥有的文件大小数据
        u.setOwnFileSize(u.getOwnFileSize() - f.getSize());
        User newUser = new User();
        newUser.setId(u.getId());
        newUser.setOwnFileSize(u.getOwnFileSize());
        userMapper.updateByPrimaryKeySelective(newUser);

        // 删除保存的文件
        fileSaver.deleteImage(f.getCoverImagePath());
        fileSaver.deleteVideo(f.getFilePath());

        // 删除数据库记录
        fileMapper.deleteByPrimaryKey(id);
        return 1;
    }

}
