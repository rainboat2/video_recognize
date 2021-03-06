package com.neu.video_recognize.service.user;

import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.entity.vo.RegisterInformation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    User selectUserByPrimaryKey(Integer id);

    Map<String, Object> login(String email, String password);

    Map<String, Object> register(RegisterInformation registerInfo);

    Map<String, Object> changeAvatar(MultipartFile avatar, Integer uId) throws IOException;

    Map<String, Object> updateUserInfo(Integer uId, RegisterInformation newInfo);

    String resetSecretKey(Integer uId);

    User tryToResetInvokeTime(User u);
}
