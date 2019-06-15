package com.example.semesterschedulingapp.model;

public class Users {

    private String userEmail;
    private String userPassword;
    private String userToken;
    private String tokenType;


    public Users(String userEmail, String userPassword, String userToken, String tokenType) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userToken = userToken;
        this.tokenType = tokenType;
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

    public String getTokenType() {
        return tokenType;
    }
}
