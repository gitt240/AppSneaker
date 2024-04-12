package com.example.appsneaker.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    String name;
    String des;
    String rate;
    String imgurl;
    String type;
    int price;
    String id;

    public SanPham() {
    }

    public SanPham(String name, String des, String rate, String imgurl, String type, int price) {
        this.name = name;
        this.des = des;
        this.rate = rate;
        this.imgurl = imgurl;
        this.type = type;
        this.price = price;
    }

    public SanPham(String name, String des, String rate, String imgurl, String type, int price, String id) {
        this.name = name;
        this.des = des;
        this.rate = rate;
        this.imgurl = imgurl;
        this.type = type;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
