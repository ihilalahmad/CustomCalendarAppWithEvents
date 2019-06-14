package com.example.semesterschedulingapp.model;

public class Users {

    private String userEmail;
    private String userPassword;
    private String userToken;

    public Users(String userEmail, String userPassword, String userToken) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userToken = userToken;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserToken() {
        return userToken;
    }
}
