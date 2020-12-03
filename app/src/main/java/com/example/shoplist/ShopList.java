package com.example.shoplist;

import java.util.ArrayList;

public class ShopList {
    private ArrayList<Item> shopList;

    public ShopList() {
        this.shopList = new ArrayList<>();
    }

    public ShopList( ArrayList<Item> shopList) {
        this.shopList = new ArrayList<>();
        this.setShopList(shopList);
    }

    public ArrayList<Item> getShopList() {
        return shopList;
    }

    public void setShopList(ArrayList<Item> shopList) {
        this.shopList = shopList;
    }

    public void add(Item Item){
        shopList.add(Item);
    }

    public Item get(int i){
        return shopList.get(i);
    }

}
