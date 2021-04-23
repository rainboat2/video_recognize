package com.neu.video_recognize.entity.po;

import com.neu.video_recognize.entity.vo.VideoInfo;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class File {
    private Integer id;

    private String name;

    private Integer ownerId;

    private Integer parentId;

    private Integer size;

    private String coverImagePath;

    private String filePath;

    private Date createTime;

    private Date modifyTime;

    private String recognizeResult;

    private Date lastRecognizeTime;

    public File(){}

    public File(VideoInfo fi){
        BeanUtils.copyProperties(fi, this);
        this.size = (int) fi.getVideo().getSize();
        this.name = fi.getVideo().getOriginalFilename();
        this.coverImagePath = "default.png";
        this.createTime = new Date(System.currentTimeMillis());
        this.modifyTime = new Date(System.currentTimeMillis());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRecognizeResult() {
        return recognizeResult;
    }

    public void setRecognizeResult(String recognizeResult) {
        this.recognizeResult = recognizeResult;
    }

    public Date getLastRecognizeTime() {
        return lastRecognizeTime;
    }

    public void setLastRecognizeTime(Date lastRecognizeTime) {
        this.lastRecognizeTime = lastRecognizeTime;
    }
}