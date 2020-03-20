package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "tags", indices = {@Index(value = {"tag"}, unique = true)})
public class Tag {
    @ColumnInfo(name = "tag")
    @PrimaryKey()
    public String tagName;
}
