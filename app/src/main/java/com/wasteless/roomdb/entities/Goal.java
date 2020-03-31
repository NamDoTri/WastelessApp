package com.wasteless.roomdb.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal
{
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo(name = "goalName")
    public String goalName;

    @ColumnInfo(name = "timeLeft")

    public String timeLeft;
    public Goal(String goalName, String timeLeft){
        this.goalName = goalName;
        this.timeLeft = timeLeft;
        Log.i("Database", "Goal created");
    }
}
