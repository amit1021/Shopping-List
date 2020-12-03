package com.example.shoplist;

public class Item {

    String name;
    int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public Item(String name) {
        this.name = name;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString(){
        return this.name + ", " + this.quantity;
    }
}
