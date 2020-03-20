package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity
public class Income {
    @PrimaryKey(autoGenerate = true)
    public Long incomeId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    public String date;

    @ColumnInfo(name = "amount", defaultValue = "0")
    @NonNull
    public double amount;

    @ColumnInfo(name = "description")
    public String description;

    //TODO: foreign key - wallet
    // @NonNull

    //TODO: array of tags

    @ColumnInfo(name = "source")
    public String source;

    //TODO: enum of Type
    // @NonNull
}
