package com.neu.video_recognize.mapper;

import com.neu.video_recognize.entity.po.Token;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Token record);

    int insertSelective(Token record);

    Token selectByPrimaryKey(Integer id);

    List<Token> selectByOwnId(Integer uid);

    int updateByPrimaryKeySelective(Token record);

    int updateByPrimaryKey(Token record);

    int deleteByOwnerId(Integer uid);
}