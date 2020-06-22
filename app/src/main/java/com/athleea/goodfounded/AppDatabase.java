package com.athleea.goodfounded;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Restaurant.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context){
        if(appDatabase==null){ // <= ERROR HERE, should be == null
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "Restaurant").allowMainThreadQueries().build();
        }
        return appDatabase;

    }

    public abstract RestaurantDao restaurantDao();

}
