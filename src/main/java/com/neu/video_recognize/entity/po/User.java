package com.neu.video_recognize.entity.po;

import java.util.Date;

public class User {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String password;

    private String imagePath;

    private String secretKey;

    private Long fileCapacity;

    private Long ownFileSize;

    private Integer totalInvokeTime;

    private Integer invokeTime;

    private Date invokeLastUpdateTime;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(Long fileCapacity) {
        this.fileCapacity = fileCapacity;
    }

    public Long getOwnFileSize() {
        return ownFileSize;
    }

    public void setOwnFileSize(Long ownFileSize) {
        this.ownFileSize = ownFileSize;
    }

    public Integer getTotalInvokeTime() {
        return totalInvokeTime;
    }

    public void setTotalInvokeTime(Integer totalInvokeTime) {
        this.totalInvokeTime = totalInvokeTime;
    }

    public Integer getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(Integer invokeTime) {
        this.invokeTime = invokeTime;
    }

    public Date getInvokeLastUpdateTime() {
        return invokeLastUpdateTime;
    }

    public void setInvokeLastUpdateTime(Date invokeLastUpdateTime) {
        this.invokeLastUpdateTime = invokeLastUpdateTime;
    }
}