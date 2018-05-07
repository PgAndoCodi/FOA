package com.example.pglap.orderfood.models;

import java.util.List;

public class Request {

    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private List<Order> Foods;
    private String Status;

    public Request() {
    }

    public Request(String name, String phone, String address, String total, List<Order> foods) {
        Name = name;
        Phone = phone;
        Address = address;
        Total = total;
        Foods = foods;
        Status = "0"; //Default is O.   0:Order Placed,   1:Shipping,   2:Shipped.
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getFoods() {
        return Foods;
    }

    public void setFoods(List<Order> foods) {
        Foods = foods;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
