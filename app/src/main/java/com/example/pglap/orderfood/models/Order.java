package com.example.pglap.orderfood.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Blob;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "order_table")
public class Order {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int orderid;

    @ColumnInfo(name = "productid")
    private String ProductId;

    @ColumnInfo(name = "productname")
    private String ProductName;

    @ColumnInfo(name = "quantity")
    private String Quantity;

    @ColumnInfo(name="price")
    private String Price;

    @ColumnInfo(name="discount")
    private String Discount;

    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price, String discount) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getOrderIdString() {
        return  String.valueOf(orderid);
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }



    public int getQuantityInt() {
        return Integer.valueOf(Quantity);
    }

    public int getPriceInt() {
        return Integer.valueOf(Price);
    }
}
