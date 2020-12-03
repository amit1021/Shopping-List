package com.example.shoplist;

import java.util.ArrayList;

public class User {
    private String user;
    private String email;
    private String password;
    private String passwordAuth;
    public ArrayList<String> shopListUID;

    public User(){}

    public User(String user, String email, String password){
        this.user = user;
        this.email = email;
        this.password = password;
        shopListUID = new ArrayList<>();
    }

    public ArrayList<String> getShopListUID() {
        return shopListUID;
    }

    public void setShopListUID(ArrayList<String> shopListUID) {
        this.shopListUID = shopListUID;
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