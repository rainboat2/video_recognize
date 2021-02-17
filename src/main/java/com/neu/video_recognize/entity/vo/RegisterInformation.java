package com.neu.video_recognize.entity.vo;

public class RegisterInformation {

    String phone;
    String password;
    String email;
    String name;

    public RegisterInformation(){}

    public RegisterInformation(String phone, String password, String email, String name) {
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
