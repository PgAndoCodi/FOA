package com.example.pglap.orderfood.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.models.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM order_table")
    LiveData<List<Order>> getOrderData();

    @Query("SELECT * FROM order_table WHERE orderid IN (:orderId)")
    LiveData<Order> getOrderIdData(String orderId);

    @Insert
    void insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("DELETE FROM order_table WHERE orderid IN (:orderId)")
    void deleteOrderById(String orderId);

    @Query("DELETE FROM order_table")
    void deleteAllOrder();
}