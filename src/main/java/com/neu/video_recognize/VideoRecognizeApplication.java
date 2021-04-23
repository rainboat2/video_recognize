package com.neu.video_recognize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.neu.video_recognize.mapper")
public class VideoRecognizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoRecognizeApplication.class, args);
    }


}
