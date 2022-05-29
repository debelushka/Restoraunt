package com.example.rpm.modelsDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Restaurant {
    @PrimaryKey(autoGenerate = true) public int id;

    @ColumnInfo(name = "name") public String name;

    @Ignore public boolean newObj;
    @Ignore public boolean changed;

    @Ignore public static Restaurant findFacultyById(ArrayList<Restaurant> searchingList, int facultyId){
        for(Restaurant restaurant :searchingList){
            if(restaurant.id == facultyId){
                return restaurant;
            }
        }
        return null;
    }
}
