package com.example.appsneaker.model;

import java.io.Serializable;

public class GioHang implements Serializable {
    String name;
    String quantity;
    String imgurl;
    int price;
    String id;
    public GioHang() {
    }

    public GioHang(String name, String quantity, String imgurl, int price) {
        this.name = name;
        this.quantity = quantity;
        this.imgurl = imgurl;
        this.price = price;
    }

    public GioHang(String name, String quantity, String imgurl, int price, String id) {
        this.name = name;
        this.quantity = quantity;
        this.imgurl = imgurl;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
