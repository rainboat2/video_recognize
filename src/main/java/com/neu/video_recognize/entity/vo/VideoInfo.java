package com.neu.video_recognize.entity.vo;

import org.springframework.web.multipart.MultipartFile;

public class VideoInfo {

    private MultipartFile video;
    private Integer parentId;
    private Integer ownerId;

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
