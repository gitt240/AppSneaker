package com.example.appsneaker.model;

public class Loai {
    String name;
    String imgUrl;
    String type;

    public Loai() {
    }

    public Loai(String name, String imgUrl, String type) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
