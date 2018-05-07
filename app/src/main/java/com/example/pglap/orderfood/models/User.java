package com.example.pglap.orderfood.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    private int uid;
    private String phone;


    /** Only these 2 columns will be saved online ***/
    /**/                                          /**/
    /**/private String name;                      /**/
    /**/                                          /**/
    /**/private String password;                  /**/
    /**/                                          /**/
    /**//**//**//**//**//**//**//**//**//**//**//*//*/


    //Instruction for adding new field
    /**
     * STEPS TO ADD NEW MEMBER VARIABLE
     *
     * a.Add Member variable
     * b.Include in constructor
     * c.Getter & Setter
     *
     * */


    //CONSTRUCTORS
    @Ignore
    public User() {
    }
    public User(String name, String password) {
        this.uid = Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(3, 11));
        this.name = name;
        this.password = password;
    }


    //GETTERS

    public int getUid() {
        return uid;
    }
    public String getPhone() {
        return phone;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }



    //SETTERS
    public void setUid(int uid) {
        this.uid = uid;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
