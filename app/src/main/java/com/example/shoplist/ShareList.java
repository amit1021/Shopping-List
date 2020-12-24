package com.example.shoplist;

public class ShareList {
    private String nameContact;
    private String addressContact;
    private String phoneContact;
    private String listUid;
    private String nameList;

    public ShareList(String nameContact, String addressContact, String phoneContact, String listUid, String nameList) {
        this.nameContact = nameContact;
        this.addressContact = addressContact;
        this.phoneContact = phoneContact;
        this.listUid = listUid;
        this.nameList = nameList;
    }

    public String getNameContact() {
        return nameContact;
    }

    public String getAddressContact() {
        return addressContact;
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

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public void setAddressContact(String addressContact) {
        this.addressContact = addressContact;
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
}
