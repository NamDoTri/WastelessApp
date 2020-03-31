package com.wasteless.roomdb.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.wasteless.roomdb.entities.Goal;

@Dao
public interface GoalDao {

    @Insert(entity = Goal.class)
    void insertAll(Goal... goals);
}
