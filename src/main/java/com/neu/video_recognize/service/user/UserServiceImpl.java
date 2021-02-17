package com.neu.video_recognize.service.user;

import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.RegisterInformation;
import com.neu.video_recognize.mapper.FileSaver;
import com.neu.video_recognize.mapper.TokenMapper;
import com.neu.video_recognize.mapper.UserMapper;
import com.neu.video_recognize.tool.Tools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileSaver fileSaver;

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public User selectUserByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> rs = new HashMap<>();
        User u = userMapper.selectByEmail(email);
        if (u == null){
            rs.put("status", 0);
            rs.put("msg", "用户名不存在");
        }else if (!u.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            rs.put("status", 0);
            rs.put("msg", "密码错误");
        }else{
            u.setPassword(null);
            rs.put("user", u);
            rs.put("status", 1);
        }
        return rs;
    }

    @Override
    public Map<String, Object> register(RegisterInformation registerInfo) {
        Map<String, Object> rs = new HashMap<>();

        // 确保邮箱的唯一性
        if (userMapper.selectByEmail(registerInfo.getEmail()) != null){
            rs.put("status", 0);
            rs.put("msg", "该邮箱已经被注册！");
            return rs;
        }

        User u = new User();
        BeanUtils.copyProperties(registerInfo, u);
        u.setSecretKey(Tools.randomString());
        // 对密码使用md5加密
        u.setPassword(DigestUtils.md5DigestAsHex(u.getPassword().getBytes()));
        u.setInvokeLastUpdateTime(new Date(System.currentTimeMillis()));
        userMapper.insertSelective(u);

        rs.put("status", 1);
        return rs;
    }

    @Override
    public Map<String, Object> changeAvatar(MultipartFile avatar, User u) throws IOException {
        Map<String, Object> rs = new HashMap<>();

        String fileName = fileSaver.saveImage(avatar);
        User user = new User();
        user.setId(u.getId());
        user.setImagePath(fileName);
        userMapper.updateByPrimaryKeySelective(user);
        // 更新当前用户的信息
        u.setImagePath(fileName);

        rs.put("state", 1);
        rs.put("msg", "修改头像成功");
        rs.put("user", u);
        return rs;
    }

    @Override
    public Map<String, Object> updateUserInfo(User u, RegisterInformation newInfo) {
        Map<String, Object> rs = new HashMap<>();
        // 构建一个user对象，用于选择性更新数据库中的用户数据
        User updateUser = new User();
        updateUser.setId(u.getId());
        BeanUtils.copyProperties(newInfo, updateUser);
        if (updateUser.getPassword() != null)
            updateUser.setPassword(md5(updateUser.getPassword()));


        // 更新用户信息
        userMapper.updateByPrimaryKeySelective(updateUser);

        // 更新session中的用户信息
        User newUser = userMapper.selectByPrimaryKey(u.getId());
        BeanUtils.copyProperties(newUser, u);
        u.setPassword(null);

        rs.put("status", 1);
        rs.put("msg", "修改用户信息成功");
        rs.put("user", u);
        return rs;
    }

    @Override
    public void resetSecretKey(User u) {
        String sk;
        do {
            sk = Tools.randomString();
        } while (userMapper.selectBySecretKey(sk) != null);

        // 更新secretKey
        User user = new User();
        user.setId(u.getId());
        user.setSecretKey(sk);
        userMapper.updateByPrimaryKeySelective(user);
        u.setSecretKey(sk);

        // 删除所有相关的token
        tokenMapper.deleteByOwnerId(u.getId());
    }

    private String md5(String s){
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }
}
