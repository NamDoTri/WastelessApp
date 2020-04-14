package com.wasteless.roomdb.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wasteless.roomdb.entities.Achievement;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AchievementDao {
    @Query("select * from achievements")
    List<Achievement> getAllAchievements();

    @Query("update achievements set isDone = 1 where name =:name")
    void setAchievementToBeDone(String name);

    @Insert(entity = Achievement.class, onConflict = OnConflictStrategy.IGNORE)
    void insert(Achievement achievement);

    @Update(entity = Achievement.class, onConflict = OnConflictStrategy.IGNORE)
    int update(Achievement achievement);

    @Delete
    void delete(Achievement achievement);

    @Insert(entity = Achievement.class, onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(Achievement... achievements);
}
