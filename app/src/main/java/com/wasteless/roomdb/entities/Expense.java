package com.wasteless.roomdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")

public class Expense {
    @ColumnInfo(name = "index")
    @PrimaryKey(autoGenerate = true)
    public Long index;

    @Embedded()
    Transaction transaction;

    //TODO: validator before inserting
    @ColumnInfo(name = "category")
    public String category;
}