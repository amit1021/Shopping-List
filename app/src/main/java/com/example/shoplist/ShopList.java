package com.example.shoplist;

import android.provider.Contacts;

import com.example.shoplist.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopList  {
    private String name;
    private String UID;
    private ArrayList <Item> items;

    public ShopList() {
        items = new ArrayList<>();
    }

    public ShopList(ShopList other){
        this.name = other.name;
        items = other.items;
        this.UID = other.UID;
    }

    public ShopList(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public ShopList(String name, String UID) {
        this.name = name;
        this.UID = UID;
        items = new ArrayList<>();
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

    public String toString(){
        return "  " + this.name;
    }
}
