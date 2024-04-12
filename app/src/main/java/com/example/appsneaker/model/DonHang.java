package com.example.appsneaker.model;

import java.util.ArrayList;

public class DonHang {
    String name;
    String phone;
    String address;
    int totalPrice;
    String status;
    String id;
    String date;
    String time;
    String email;
    ArrayList<GioHang> cartItem;

    public DonHang() {
    }

    public DonHang(String status, String id, String date, String time, String email) {
        this.status = status;
        this.id = id;
        this.date = date;
        this.time = time;
        this.email = email;
    }

    public DonHang(String status, String id, String date, String email) {
        this.status = status;
        this.id = id;
        this.date = date;
        this.email = email;
    }

    public DonHang(String name, String phone, String address, int totalPrice, String status, String id, String date, String time, String email, ArrayList<GioHang> cartItem) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.id = id;
        this.date = date;
        this.time = time;
        this.email = email;
        this.cartItem = cartItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<GioHang> getCartItem() {
        return cartItem;
    }

    public void setCartItem(ArrayList<GioHang> cartItem) {
        this.cartItem = cartItem;
    }
}
