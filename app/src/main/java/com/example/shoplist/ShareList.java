package com.example.shoplist;

public class ShareList {
    private String nameContact;
    private String cityContact;
    private String streetContact;
    private String phoneContact;
    private String listUid;
    private String nameList;
    private boolean selected;

    public ShareList(){
        ;
    }

    public ShareList(String nameContact, String cityContact,String streetContact, String phoneContact, String listUid, String nameList) {
        this.nameContact = nameContact;
        this.cityContact = cityContact;
        this.streetContact = streetContact;
        this.phoneContact = phoneContact;
        this.listUid = listUid;
        this.nameList = nameList;
        this.selected = false;
    }

    public String getNameContact() {
        return nameContact;
    }

    public String getCityContact() {
        return cityContact;
    }

    public String getStreetContact() {
        return streetContact;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public String getListUid() {
        return listUid;
    }

    public String getNameList() {
        return nameList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public void setCityContact(String cityContact) {
        this.cityContact = cityContact;
    }

    public void setStreetContact(String streetContact) {
        this.streetContact = streetContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public void setListUid(String listUid) {
        this.listUid = listUid;
    }

    public void setNameList(String nameList) {
        this.nameList = nameList;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String toString() {
        return nameContact + "\n" + cityContact + ", " + streetContact + "\n" + phoneContact;
    }
}
