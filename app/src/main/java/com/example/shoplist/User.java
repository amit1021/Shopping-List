package com.example.shoplist;

public class User {
    private String user;
    private String email;
    private String password;
    private String passwordAuth;

    public User(){}

    public User(String user, String email, String password){
        this.user = user;
        this.email = email;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordAuth() {
        return passwordAuth;
    }
}