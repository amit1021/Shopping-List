package com.example.shoplist;

import com.example.shoplist.Item;

import java.util.ArrayList;

public class ShopList {
    private String name;
    private ArrayList <Item> items;

    public ShopList() {
        ;
    }

    public ShopList(String name) {
        this.name = name;
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
}
