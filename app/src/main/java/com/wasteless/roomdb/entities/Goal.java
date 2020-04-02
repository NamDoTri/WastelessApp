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

    @ColumnInfo(name = "goalType")
    public String goalType;

    @ColumnInfo(name = "timeOfCreation")
    public String timeOfCreation;
    
    @ColumnInfo(name = "amountOfMoney")
    public double amountOfMoney;

    public Goal(String goalType, String timeOfCreation, double amountOfMoney){
        this.goalType = goalType;
        this.timeOfCreation = timeOfCreation;
        this.amountOfMoney = amountOfMoney;
        Log.i("Database", "Goal "+ goalType +". Sum " + amountOfMoney);
    }


}
