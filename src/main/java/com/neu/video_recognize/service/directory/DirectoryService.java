package com.neu.video_recognize.service.directory;


import com.neu.video_recognize.entity.po.Directory;
import com.neu.video_recognize.entity.po.User;

import java.util.Map;

public interface DirectoryService {

    Map<String, Object> getFilesAndDirectoryBy(Integer directoryId, Integer userId);

    int addDirectory(Directory d);

    int rename(Integer directoryId, String name);

    int deleteByPrimaryKey(Integer directoryId, User u);
}
