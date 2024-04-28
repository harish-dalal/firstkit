package com.example.firstkit.Model;

public class Users {

    private String name, password, phone, address, Uid , Email , alternatemobile;

    public Users()
    {

    }

    public Users(String name, String password, String phone, String address, String Uid, String Email , String alternatemobile) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.Uid = Uid;
        this.Email = Email;
        this.alternatemobile = alternatemobile;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() { return address; }

    public String getUid() { return Uid; }

    public String getEmail() { return Email; }

    public void setAddress(String address) { this.address = address; }

    public void setUid(String uid) { Uid = uid; }

    public void setEmail(String email) { Email = email; }

    public String getAlternatemobile() { return alternatemobile; }

    public void setAlternatemobile(String alternatemobile) { this.alternatemobile = alternatemobile; }

}
