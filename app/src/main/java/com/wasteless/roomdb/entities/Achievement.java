package com.wasteless.roomdb.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String  description;

    @ColumnInfo(name = "isDone")
    public boolean isDone;

    public Achievement(String name, String description, boolean isDone) {
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        Log.i("database", "Achievement created"+ name);
    }
}