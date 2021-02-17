package com.neu.video_recognize.service.file;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.VideoInfo;

import java.io.IOException;

public interface FileService {

    File getFileByPrimaryKey(Integer id);

    File saveVideo(VideoInfo fi, User u) throws IOException;

    int rename(Integer fileId, String name);

    int deleteByPrimaryKey(Integer id, User u);
}
