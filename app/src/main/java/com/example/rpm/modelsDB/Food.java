package com.example.rpm.modelsDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(
        entity = FoodPodC.class,
        parentColumns = "id",
        childColumns = "podCid", onDelete = CASCADE))
public class Food {
    @PrimaryKey (autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "width")
    public String width;


    public int podCid;
}
