package com.example.shoplist;

public class UserPermission {
    private String userUid;
    private String email;
    private String role;

    public UserPermission(){
        ;
    }

    public UserPermission(String userUid, String email, String role) {
        this.userUid = userUid;
        this.email = email;
        this.role = role;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toString(){
        return email + "    " + role;
    }
}
