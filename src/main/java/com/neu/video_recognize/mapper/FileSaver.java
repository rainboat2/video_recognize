package com.neu.video_recognize.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileSaver {

    @Value("${upload.video}")
    private String videoPath;

    @Value("${upload.image}")
    private String imagePath;

    public String saveVideo(MultipartFile video) throws IOException {
        String saveFileName = UUID.randomUUID().toString().replaceAll("-", "") + video.getOriginalFilename();
        File f = new File(videoPath + "/" + saveFileName);
        video.transferTo(new File(f.getAbsolutePath()));
        return saveFileName;
    }

    public String  saveImage(MultipartFile image) throws IOException{
        String saveFileName = UUID.randomUUID().toString().replaceAll("-", "") + image.getOriginalFilename();
        File f = new File(imagePath + "/" + saveFileName);
        image.transferTo(new File(f.getAbsolutePath()));
        return saveFileName;
    }

    public boolean deleteVideo(String name){
        String path = videoPath + "/" + name;
        return delete(path);
    }

    public boolean deleteImage(String name){
        if (name.equals("default.png"))
            return true;
        return delete(imagePath + "/" + name);
    }

    private boolean delete(String path){
        File f = new File(path);
        return f.delete();
    }
}
