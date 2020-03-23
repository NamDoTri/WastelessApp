package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.*;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagDao {
    @Query("select * from tags")
    List<Tag> getAll();

    @Query("select * from tags where tag like :name")
    List<Tag> getTagsByName(String name);

    @Insert(entity = Tag.class)
    void insertAll(Tag... tag);

    @Update(entity = Tag.class)
    void updateAll(Tag... tag);

    @Delete
    void delete(Tag tag);
}
