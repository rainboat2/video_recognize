package com.neu.video_recognize.mapper;

import com.neu.video_recognize.entity.po.File;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

    List<File> selectByOwnerAndParent(File record);

    List<File> selectByParentId(Integer parentId);
}