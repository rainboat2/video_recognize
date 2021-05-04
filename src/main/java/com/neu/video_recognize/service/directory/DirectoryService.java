package com.neu.video_recognize.service.directory;


import com.neu.video_recognize.entity.po.Directory;
import com.neu.video_recognize.entity.po.User;

import java.util.List;
import java.util.Map;

public interface DirectoryService {

    Map<String, Object> getFilesAndDirectoryBy(Integer directoryId, Integer userId);

    List<Directory> getDirectories(Integer directoryId, Integer userId);

    int addDirectory(Directory d);

    int rename(Integer directoryId, String name);

    int deleteByPrimaryKey(Integer directoryId, Integer userId);

    int moveFile(Integer fileId, Integer newParentId, Boolean isDirectory, Map<String, Object> rs);
}
