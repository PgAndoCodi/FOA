package com.example.pglap.orderfood.models;

public class Address {

    private int uid;
    private String HouseNo; //
    private String Locality; //
    private String State;
    private String Country;
    private String Pincode;

    public Address() {
    }

    public Address(String houseNo, String locality, String state, String country, String pincode) {
        HouseNo = houseNo;
        Locality = locality;
        State = state;
        Country = country;
        Pincode = pincode;
    }




    //GETTERS & SETTERS

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }
}
