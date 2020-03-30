package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "tags", indices = {@Index(value = {"tag"}, unique = true)})
public class Tag {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "tag")
    public String tagName;

    public Tag(String tagName){
        this.tagName= tagName;
    }
}
