package com.neu.video_recognize.controller;

import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.RegisterInformation;
import com.neu.video_recognize.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/getById")
    public Map<String, Object> getUserById(Integer id){
        Map<String, Object> rs = new HashMap<String, Object>();
        User u = userService.selectUserByPrimaryKey(id);
        if (u != null)
            u.setPassword(null);
        rs.put("user", u);
        return rs;
    }

    @RequestMapping("/getCurrentUser")
    public Map<String, Object> getCurrentUser(HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        Map<String, Object> rs = new HashMap<>();
        User u = userService.selectUserByPrimaryKey(uId);
        rs.put("status", 1);
        rs.put("user", userService.tryToResetInvokeTime(u));
        return rs;
    }

    @RequestMapping("/login")
    public Map<String, Object> login(String email, String password, HttpSession session){
        Map<String, Object> rs = userService.login(email, password);
        if (rs.get("user") != null) {
            session.setAttribute("userId", ((User) rs.get("user")).getId());
        }else {
            session.invalidate();
        }
        return rs;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody RegisterInformation registerInfo){
        return userService.register(registerInfo);
    }

    @RequestMapping("/logout")
    public Map<String, Object> logout(HttpSession session){
        session.invalidate();
        return Collections.singletonMap("status", 1);
    }

    @RequestMapping("/changeAvatar")
    public Map<String, Object> changeAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session) throws IOException {
        Integer uId = (Integer) session.getAttribute("userId");
        return userService.changeAvatar(avatar, uId);
    }

    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public Map<String, Object> updateUserInfo(@RequestBody RegisterInformation registerInfo, HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        return userService.updateUserInfo(uId, registerInfo);
    }

    @RequestMapping("/resetSecretKey")
    public Map<String, Object> reset(HttpSession session){
        Integer uId = (Integer) session.getAttribute("userId");
        String sk = userService.resetSecretKey(uId);
        Map<String, Object> rs = new HashMap<>();
        rs.put("status", 1);
        rs.put("secretKey", sk);
        return rs;
    }

}
