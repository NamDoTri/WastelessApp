package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Expense;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("select * from expense")
    List<Expense> getAll();

    @Query("select * from expense where expenseId in (:expenseId)")
    Expense getExpenseById(int expenseId);

    @Query("select * from expense where date like :date")
    List<Expense> getExpensesByDate(String date);

    //TODO: get expenses by wallet

    //TODO: get expenses by category

    //TODO: get expenses by tag

    @Insert(entity = Expense.class)
    void insertAll(Expense... expense);

    @Update(entity = Expense.class)
    void updateAll(Expense... expense);

    @Delete
    void delete(Expense expense);
}
