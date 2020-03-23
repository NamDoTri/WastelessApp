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
    @Query("select * from expenses")
    List<Expense> getAll();

    @Query("select * from expenses where transactionId in (:expenseId)")
    Expense getExpenseById(int expenseId);

    @Query("select * from expenses where date like :date")
    List<Expense> getExpensesByDate(String date);

    @Query("select * from expenses where wallet = :walletId")
    List<Expense> getExpensesByWallet(Long walletId);

    @Query("select * from expenses where category = :category")
    List<Expense> getExpensesByCategory(String category);

    @Query("select * from expenses where transactionId in (select transactionId from tag_assoc where tag = :name)")
    List<Expense> getExpensesByTag(String name);

    @Insert(entity = Expense.class)
    void insertAll(Expense... expense);

    @Update(entity = Expense.class)
    void updateAll(Expense... expense);

    @Delete
    void delete(Expense expense);
}
