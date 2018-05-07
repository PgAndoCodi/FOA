package com.example.pglap.orderfood.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.pglap.orderfood.models.Order;
import com.example.pglap.orderfood.models.User;


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = {User.class, Order.class},
        version = UserRoomDatabase.DATABASE_VERSION,
        exportSchema = false)
public abstract class UserRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "user_database";
    public static final int DATABASE_VERSION = 1;
    public abstract UserDao userDao();
    public abstract OrderDao orderDao();


    /** Creating a SINGLETON for database */
    // Creating a getter method for that variable this will return database if called first time thereafter it will just return the previous instance

    private static UserRoomDatabase INSTANCE;
    static UserRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Establishing connection to database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDatabase.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void destroyInstance() {
        INSTANCE = null;
    }
}
