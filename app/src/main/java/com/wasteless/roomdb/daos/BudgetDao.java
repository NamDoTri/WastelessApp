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
    @Query("select * from budgets where isMonthBudget = 1")
    List<Budget> getMonthBudget();

    @Insert(entity = Budget.class, onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(Budget budget);

    @Update(entity = Budget.class, onConflict = OnConflictStrategy.IGNORE)
    int update(Budget budget);

    @Delete
    void delete(Budget budget);

}
