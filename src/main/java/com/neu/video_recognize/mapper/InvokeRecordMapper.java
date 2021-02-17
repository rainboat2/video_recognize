package com.neu.video_recognize.mapper;

import com.neu.video_recognize.entity.po.InvokeRecord;

public interface InvokeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvokeRecord record);

    int insertSelective(InvokeRecord record);

    InvokeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvokeRecord record);

    int updateByPrimaryKey(InvokeRecord record);
}