package com.neu.video_recognize.service.file;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.vo.VideoInfo;

import java.io.IOException;
import java.util.List;

public interface FileService {

    File getFileByPrimaryKey(Integer id);

    File saveVideo(VideoInfo fi) throws IOException;

    int rename(Integer fileId, String name);

    int deleteByPrimaryKey(Integer id, Integer uId);

    List<File> getAllFilesById(List<Integer> ids);
}
