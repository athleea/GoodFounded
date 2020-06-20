package com.athleea.goodfounded;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Restaurant.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RestaurantDao restaurantDao();

}
