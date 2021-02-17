package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.Token;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/getAll")
    public Map<String, Object> getAll(HttpSession session){
        User u = (User) session.getAttribute("user");
        Map<String, Object> rs = new HashMap<>();
        rs.put("status", 1);
        rs.put("tokens", tokenService.getToken(u.getId()));
        return rs;
    }

    @RequestMapping(value = "/addToken", method = RequestMethod.POST)
    public Map<String, Object> addToken(@RequestBody Token token, HttpSession session){
        User u = (User) session.getAttribute("user");
        tokenService.addToken(token, u);
        return Collections.singletonMap("status", 1);
    }
}
