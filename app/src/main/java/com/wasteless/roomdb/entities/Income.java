package com.wasteless.roomdb.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "incomes")

public class Income {
    @ColumnInfo(name = "index")
    @PrimaryKey()
    public Long index;

    @Embedded
    public Transaction transaction;

    @ColumnInfo(name = "source")
    public String source;

    //TODO: validator before inserting/updating
    @ColumnInfo(name = "type")
    public String type;
}
