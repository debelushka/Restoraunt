package com.example.rpm.modelsDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = Restaurant.class,
        parentColumns = "id",
        childColumns = "categoriesId", onDelete = CASCADE))
public class FoodPodC {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;



    public int categoriesId;

    @Ignore public boolean newObj;
    @Ignore public boolean changed;
}
