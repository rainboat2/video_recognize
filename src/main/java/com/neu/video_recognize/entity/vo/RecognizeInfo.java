package com.neu.video_recognize.entity.vo;


import org.springframework.web.multipart.MultipartFile;

public class RecognizeInfo {

    MultipartFile video;
    String token;

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
