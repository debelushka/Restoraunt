package com.example.rpm.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodPodCDao {
    @Query("SELECT * FROM FoodPodC")
    List<FoodPodC> getAll();

    @Query("SELECT * FROM FoodPodC WHERE CategoriesId IS :facultyId")
    List<FoodPodC> getFacultyDepartments(int facultyId);

    @Query("SELECT * FROM FoodPodC WHERE id = :id")
    FoodPodC getOneById(int id);

    @Insert
    void insertAll(FoodPodC... foodPodCS);

    @Update
    void update(FoodPodC foodPodC);

    @Delete
    void delete(FoodPodC foodPodC);
}
