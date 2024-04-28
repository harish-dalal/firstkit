package com.example.firstkit.Model;

public class Products
{
    private String product_name , price , description , image , pid , date , time;

    public Products()
    {

    }

    public Products(String product_name, String price, String description, String image, String pid, String date, String time) {
        this.product_name = product_name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
}
