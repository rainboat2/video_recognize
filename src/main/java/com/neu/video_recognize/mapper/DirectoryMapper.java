package com.neu.video_recognize.mapper;

import com.neu.video_recognize.entity.po.Directory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DirectoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Directory record);

    int insertSelective(Directory record);

    Directory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Directory record);

    int updateByPrimaryKey(Directory record);

    List<Directory> selectByOwnerAndParent(Directory info);

    List<Directory> selectByParentId(Integer parentId);
}