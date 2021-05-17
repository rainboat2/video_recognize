package com.neu.video_recognize.service.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.neu.video_recognize.entity.po.Token;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.mapper.TokenMapper;
import com.neu.video_recognize.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Token> getToken(Integer uid) {
        return tokenMapper.selectByOwnId(uid);
    }

    @Override
    public boolean addToken(Token token, Integer uId) {
        long validTime = token.getExpireTime().getTime() - System.currentTimeMillis();
        token.setCreateTime(new Date(System.currentTimeMillis()));

        User u = userMapper.selectByPrimaryKey(uId);
        token.setContent(token(u.getSecretKey(), u.getId(), validTime));
        token.setOwnerId(u.getId());
        tokenMapper.insert(token);
        return true;
    }

    public boolean verify(String token, String secretKey){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    @Override
    public Integer getClaimedUserId(String token) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        }catch (Exception e){
            return null;
        }
        return jwt.getClaim("userId").asInt();
    }


    private String token(String secretKey, Integer userId, long validTime){
        Date date = new Date(System.currentTimeMillis() + validTime);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Map<String,Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create()
                .withHeader(header)
                .withClaim("userId", userId)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
