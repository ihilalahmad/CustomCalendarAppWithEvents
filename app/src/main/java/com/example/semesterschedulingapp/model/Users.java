package com.example.semesterschedulingapp.model;

public class Users {

    private String userEmail;
    private String userPassword;
    private String userToken;
    private String tokenType;
    private String userProgram_id;


    public Users(String userEmail, String userPassword, String userToken, String tokenType, String userProgram_id) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userToken = userToken;
        this.tokenType = tokenType;
        this.userProgram_id = userProgram_id;
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

    public String getUserProgram_id(){

        return userProgram_id;
    }
}
