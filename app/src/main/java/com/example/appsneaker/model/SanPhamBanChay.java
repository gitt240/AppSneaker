package com.example.appsneaker.model;

import java.io.Serializable;

public class SanPhamBanChay implements Serializable {
    String name;
    String rate;
    String type;
    String des;
    String imgurl;
    int price;

    public SanPhamBanChay() {
    }

    public SanPhamBanChay(String name, String rate, String type, String description, String imgurl, int price) {
        this.name = name;
        this.rate = rate;
        this.type = type;
        this.des = description;
        this.imgurl = imgurl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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
}
