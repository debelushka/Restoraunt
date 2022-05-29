package com.example.rpm.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface RestaurantDao {
    @Query("SELECT * FROM Restaurant")
    List<Restaurant> getAll();

    @Query("SELECT * FROM Restaurant WHERE id = :id")
    List<Restaurant> getById(int id);

    @Query("SELECT * FROM Restaurant WHERE id = :id")
    Restaurant getOneById(int id);

    @Insert
    void insertAll(Restaurant... faculties);

    @Update
    void update(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);
}
