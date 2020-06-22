package com.athleea.goodfounded;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collections;
import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM Restaurant")
    List<Restaurant> getAll();

    @Insert
    void insert(Restaurant rs);

    @Update
    void update(Restaurant rs);

    @Delete
    void delete(Restaurant rs);

    @Query("SELECT * FROM Restaurant WHERE '업태' like :type AND '경도' >= :bottom AND '경도' <= :top AND '위도' >= :left AND '위도' <= :right")
    List<Restaurant> search(String type, double left, double right, double top, double bottom);




}
