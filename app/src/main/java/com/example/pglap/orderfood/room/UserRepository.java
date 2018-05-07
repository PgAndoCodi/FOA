package com.example.pglap.orderfood.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.models.User;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    private UserDao mUserDao;
    private OrderDao mOrderDao;

    private LiveData<User> mUsersData;
    private LiveData<List<Order>> mOrderData;

    UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mOrderDao = db.orderDao();
        mUsersData = mUserDao.getUserData();
        mOrderData = mOrderDao.getOrderData();
    }

    LiveData<User> getUserData() {
        return mUsersData;
    }
    LiveData<List<Order>> getOrderData() {
        return mOrderData;
    }


    public void insertUser (User user) {
        new insertUserAsyncTask(mUserDao).execute(user);
    }
    public void updateUser (User user) {
        new updateUserAsyncTask(mUserDao).execute(user);
    }
    public void deleteUser (User user) {
        new deleteUserAsyncTask(mUserDao).execute(user);
    }

    public void insertOrder (Order order) {
        new insertOrderAsyncTask(mOrderDao).execute(order);
    }
    public void updateOrder (Order order) {
        new updateOrderAsyncTask(mOrderDao).execute(order);
    }
    public void deleteOrder (String orderId) {
        new deleteOrderAsyncTask(mOrderDao).execute(orderId);
    }
    public void deleteAllOrder () {
        new deleteAllOrderAsyncTask(mOrderDao).execute();
    }

    //public void queryOrderById (String orderID) { new queryOrderAsynTask(mUserDao).execute(orderID); }

    /**THIS IS A PART WHERE RXJAVA CAN BE USED, as AsyncTask is not lifecycle aware*/
    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }
    private static class updateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.updateUser(params[0]);
            return null;
        }
    }
    private static class deleteUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.deleteUser(params[0]);
            return null;
        }
    }

    private static class insertOrderAsyncTask extends AsyncTask<Order, Void, Void> {

        private OrderDao mAsyncTaskDao;

        insertOrderAsyncTask(OrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Order... params) {
            mAsyncTaskDao.insertOrder(params[0]);
            return null;
        }
    }
    private static class updateOrderAsyncTask extends AsyncTask<Order, Void, Void> {

        private OrderDao mAsyncTaskDao;

        updateOrderAsyncTask(OrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Order... params) {
            mAsyncTaskDao.updateOrder(params[0]);
            return null;
        }
    }
    private static class deleteOrderAsyncTask extends AsyncTask<String, Void, Void> {

        private OrderDao mAsyncTaskDao;

        deleteOrderAsyncTask(OrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncTaskDao.deleteOrderById(strings[0]);
            return null;
        }
    }
    private static class deleteAllOrderAsyncTask extends AsyncTask<Void, Void, Void> {

        private OrderDao mAsyncTaskDao;

        deleteAllOrderAsyncTask(OrderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            mAsyncTaskDao.deleteAllOrder();
            return null;
        }
    }

    //    private static class queryOrderAsynTask extends AsyncTask<String, Void, Void> {
//
//        private UserDao mAsyncTaskDao;
//
//        queryOrderAsynTask(UserDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            mAsyncTaskDao.getOrderIdData(strings[0]);
//            return null;
//        }
//    }
}
