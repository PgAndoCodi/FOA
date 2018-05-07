package com.example.pglap.orderfood.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.models.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    private LiveData<User> mUsersData;
    private LiveData<List<Order>> mOrderData;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mUsersData = mRepository.getUserData();
        mOrderData = mRepository.getOrderData();
    }

    public LiveData<User> queryUserData() { return mUsersData; }

    public void insertUser(User user) { mRepository.insertUser(user); }

    public void updateUser(User user) { mRepository.updateUser(user); }

    public void deleteUser(User user) { mRepository.deleteUser(user); }



    public LiveData<List<Order>> queryOrderData() { return mOrderData; }

    public void insertOrder(Order order) { mRepository.insertOrder(order); }

    public void updateOrder(Order order) { mRepository.updateOrder(order); }

    public void deleteOrder(String orderId) { mRepository.deleteOrder(orderId); }

    public void deleteAllOrder() { mRepository.deleteAllOrder(); }
}