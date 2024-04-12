package com.example.appsneaker.model;

public class Address {
    String address;
    boolean addressSelected;

    public Address() {
    }

    public Address(String address, boolean addressSelected) {
        this.address = address;
        this.addressSelected = addressSelected;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAddressSelected() {
        return addressSelected;
    }

    public void setAddressSelected(boolean addressSelected) {
        this.addressSelected = addressSelected;
    }
}
