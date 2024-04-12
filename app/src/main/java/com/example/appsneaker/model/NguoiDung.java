package com.example.appsneaker.model;

public class NguoiDung {
    String email;
    String pass;
    String phone;
    String name;
    String image;

    public NguoiDung() {
    }

    public NguoiDung(String name) {
        this.name = name;
    }

    public NguoiDung(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public NguoiDung(String email, String pass, String phone, String name) {
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
    }

    public NguoiDung(String email, String pass, String phone, String name, String image) {
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
