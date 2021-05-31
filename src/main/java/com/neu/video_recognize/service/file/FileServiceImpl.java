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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public File saveVideo(VideoInfo fi) throws IOException {
        // 存储文件到磁盘
        String videoFilePathName = fileSaver.saveVideo(fi.getVideo());

        String imageFilePathName = "default.png";
        if (fi.getPoster() != null)
            imageFilePathName = fileSaver.saveImage(fi.getPoster());

        // 保存相关记录到数据库
        File file = new File(fi);
        file.setFilePath(videoFilePathName);
        file.setCoverImagePath(imageFilePathName);
        file.setLastRecognizeTime(new Date(86400000));
        file.setRecognizeResult("");
        fileMapper.insert(file);

        // 更新用户的存储空间使用量
        User u = userMapper.selectByPrimaryKey(fi.getOwnerId());
        User updateInfo = new User();
        updateInfo.setOwnFileSize(u.getOwnFileSize() + fi.getVideo().getSize());
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
    public int deleteByPrimaryKey(Integer id, Integer uId) {
        File f = fileMapper.selectByPrimaryKey(id);

        // 更新用户拥有的文件大小数据
        User u = userMapper.selectByPrimaryKey(uId);
        User newUser = new User();
        newUser.setId(u.getId());
        newUser.setOwnFileSize(u.getOwnFileSize() - f.getSize());
        userMapper.updateByPrimaryKeySelective(newUser);

        // 删除保存的文件
        fileSaver.deleteImage(f.getCoverImagePath());
        fileSaver.deleteVideo(f.getFilePath());

        // 删除数据库记录
        fileMapper.deleteByPrimaryKey(id);
        return 1;
    }

    @Override
    public List<File> getAllFilesById(List<Integer> ids) {
        List<File> files = new ArrayList<>(ids.size());
        for (Integer id : ids){
            files.add(fileMapper.selectByPrimaryKey(id));
        }
        return files;
    }

}
