package com.wasteless.roomdb.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public Long expenseId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    public String date;

    @ColumnInfo(name = "amount")
    @NonNull
    public double amount;

    @ColumnInfo(name = "description")
    public String description;

    //foreign key - wallet
    // @NonNull

    //array of tags

    //enum of Category
    //@NonNull
}