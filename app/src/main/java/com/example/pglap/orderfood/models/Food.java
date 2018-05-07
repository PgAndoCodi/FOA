package com.example.pglap.orderfood.models;

public class Food {

    private String Description;
    private String Discount;
    private String Image;
    private String Price;
    private String Name;
    private String Menuid;

    public Food() {
    }

    public Food(String description, String discount, String image, String price, String name, String menuid) {
        Description = description;
        Discount = discount;
        Image = image;
        Price = price;
        Name = name;
        Menuid = menuid;
    }

    public String getDescription() {
        return Description;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getImage() {
        return Image;
    }

    public String getPrice() {
        return Price;
    }

    public String getName() {
        return Name;
    }

    public String getMenuid() {
        return Menuid;
    }
}