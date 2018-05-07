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
public interface UserDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.

    //USER TABLE

    @Query("SELECT * FROM user_table")
    LiveData<User> getUserData();

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

}
