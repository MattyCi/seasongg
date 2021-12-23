package com.seasongg.users.models;

public class RegistrationRequest {

    private String username;
    private String password;
    private String passwordVerify;

    public RegistrationRequest() { }

    public RegistrationRequest(String username, String password, String passwordVerify) {
        this.username = username;
        this.password = password;
        this.passwordVerify = passwordVerify;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

}
