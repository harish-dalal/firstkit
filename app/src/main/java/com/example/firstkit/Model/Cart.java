package com.example.firstkit.Model;

public class Cart
{

    private String date , disscount , pImage , pName , pPrice , pid , quantity , time;

    public Cart(){
    }

    public Cart(String date, String disscount, String pImage, String pName, String pPrice, String pid, String quantity, String time) {
        this.date = date;
        this.disscount = disscount;
        this.pImage = pImage;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pid = pid;
        this.quantity = quantity;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisscount() {
        return disscount;
    }

    public void setDisscount(String disscount) {
        this.disscount = disscount;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
