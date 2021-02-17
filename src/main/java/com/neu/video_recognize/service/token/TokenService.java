package com.neu.video_recognize.service.token;

import com.neu.video_recognize.entity.po.Token;
import com.neu.video_recognize.entity.po.User;

import java.util.List;

public interface TokenService {

    List<Token> getToken(Integer uid);

    boolean addToken(Token token, User u);
}
