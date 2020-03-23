package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Income;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IncomeDao {
    @Query("select * from incomes")
    List<Income> getAll();

    @Query("select * from incomes where transactionId in (:incomeId)")
    Income getIncomeById(int incomeId);

    @Query("select * from incomes where date like :date")
    List<Income> getIncomesByDate(String date);

    @Query("select * from incomes where transactionId in (select transactionId from tag_assoc where tag = :name)")
    List<Income> getIncomesByTag(String name);

    @Query("select * from incomes where type = :type")
    List<Income> getIncomesByType(String type);

    @Query("select * from incomes where source like :source ")
    List<Income> getIncomesBySource(String source);

    @Query("select * from incomes where wallet = :walletId")
    List<Income> getIncomesByWallet(Long walletId);

    @Insert(entity = Income.class)
    void insertAll(Income... income);

    @Update(entity = Income.class)
    void updateAll(Income... income);

    @Delete
    void delete(Income income);
}
