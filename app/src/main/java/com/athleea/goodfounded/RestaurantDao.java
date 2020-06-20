package com.athleea.goodfounded;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM Restaurant")
    LiveData<List<Restaurant>> getAll();

    @Insert
    void insert(Restaurant rs);

    @Update
    void update(Restaurant rs);

    @Delete
    void delete(Restaurant rs);

}
