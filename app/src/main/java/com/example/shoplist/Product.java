package com.example.shoplist;

public class Product {
    private String productName;
    private String productBarcode;

    public Product(){
        ;
    }
    public Product(String productName, String productBarcode){
        this.productName = productName;
        this.productBarcode = productBarcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }
}
