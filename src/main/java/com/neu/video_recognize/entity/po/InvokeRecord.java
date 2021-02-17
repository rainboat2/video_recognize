package com.neu.video_recognize.entity.po;

import java.util.Date;

public class InvokeRecord {
    private Integer id;

    private Integer invokerId;

    private Boolean isApiInvoke;

    private Date invokeTime;

    private String accessToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvokerId() {
        return invokerId;
    }

    public void setInvokerId(Integer invokerId) {
        this.invokerId = invokerId;
    }

    public Boolean getIsApiInvoke() {
        return isApiInvoke;
    }

    public void setIsApiInvoke(Boolean isApiInvoke) {
        this.isApiInvoke = isApiInvoke;
    }

    public Date getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(Date invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}