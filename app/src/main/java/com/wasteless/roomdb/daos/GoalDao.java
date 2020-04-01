package com.wasteless.roomdb.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.wasteless.roomdb.entities.Goal;

@Dao
public interface GoalDao {

    @Insert(entity = Goal.class)
    void insertAll(Goal... goals);

    @Query("select * from goals where goalType = :type")
    Goal getGoaByType(String type);

    @Query("delete from goals where goalType = :type ")
    void deleteGoalByType(String type);
}
