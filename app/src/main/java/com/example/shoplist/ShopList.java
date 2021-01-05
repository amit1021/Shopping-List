package com.example.shoplist;

import android.provider.Contacts;

import com.example.shoplist.Item;

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;

public class ShopList {
    private String name;
    private String UID;
    private boolean share;
    private ArrayList<Item> items;
    private ArrayList<UserPermission> permissions;

    public ShopList() {
        this.items = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.share = false;
    }

    public ShopList(ShopList other) {
        this.name = other.name;
        this.items = other.items;
        this.UID = other.UID;
        this.permissions = other.permissions;
        this.share = other.share;
    }

    public ShopList(String name, String UID) {
        this.name = name;
        this.UID = UID;
        this.items = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.share = false;
    }

    public boolean isShare() {
        return share;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setShare(boolean share) { this.share = share;  }

    public ArrayList<UserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public String toString() {
        return "  " + this.name;
    }
}
