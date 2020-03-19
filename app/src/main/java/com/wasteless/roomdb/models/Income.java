package com.wasteless.roomdb.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity
public class Income {
    @PrimaryKey(autoGenerate = true)
    private String incomeId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    private String date;

    @ColumnInfo(name = "amount", defaultValue = "0")
    @NonNull
    private double amount;

    @ColumnInfo(name = "description")
    private String description;

    //foreign key - wallet
    // @NonNull

    //array of tags

    @ColumnInfo(name = "source")
    private String source;

    //enum of Type
    // @NonNull
}
