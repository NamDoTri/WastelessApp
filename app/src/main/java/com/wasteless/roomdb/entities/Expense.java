package com.wasteless.roomdb.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private String expenseId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    private String date;

    @ColumnInfo(name = "amount")
    @NonNull
    private double amount;

    @ColumnInfo(name = "description")
    private String description;

    //foreign key - wallet
    // @NonNull

    //array of tags

    //enum of Category
    //@NonNull
}