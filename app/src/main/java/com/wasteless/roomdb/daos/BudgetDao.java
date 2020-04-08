package com.wasteless.roomdb.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wasteless.roomdb.entities.Budget;

import java.util.List;

@Dao
public interface BudgetDao {
    @Query("select coalesce(amount, 0.0) from budgets where isMonthBudget = 1")
    Double getMonthBudget();

    @Insert(entity = Budget.class, onConflict = OnConflictStrategy.IGNORE)
    void insert(Budget budget);

    @Update(entity = Budget.class, onConflict = OnConflictStrategy.IGNORE)
    int update(Budget budget);

    @Delete
    void delete(Budget budget);

}
