package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Transaction;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {
    // query transaction in general
    @Query("select * from transactions")
    List<Transaction> getAll();

    @Query("select * from transactions where transactionId = :transactionId")
    Transaction getTransactionById(Long transactionId);

    @Query("select * from transactions where date like :date")
    List<Transaction> getTransactionsByDate(String date);

    @Query("select * from transactions where transactionId in (select transactionId from tag_assoc where tag = :tagName)")
    List<Transaction> getTransactionsByTagName(String tagName);

    @Query("select * from transactions where wallet = :walletId")
    List<Transaction> getTransactionsByWallet(Long walletId);

    // query incomes
    @Query("select * from transactions where isIncome = 1")
    List<Transaction> getAllIncomes();

    @Query("select * from transactions where type = :type")
    List<Transaction> getIncomesByType(String type);

    @Query("select * from transactions where source like :source")
    List<Transaction> getIncomesBySource(String source);

    @Query("select * from transactions where wallet = :walletId and isIncome = 1")
    List<Transaction> getIncomesByWallet(Long walletId);

    // query expenses
    @Query("select * from transactions where isIncome = 0")
    List<Transaction> getAllExpenses();

    @Query("select * from transactions where type = :type")
    List<Transaction> getExpensesByCategory(String type);

    @Query("select * from transactions where wallet = :walletId and isIncome = 0")
    List<Transaction> getExpensesByWallet(Long walletId);

    //TODO: get incomes and expenses before and after a date

    @Insert(entity = Transaction.class)
    void insertAll(Transaction... transactions);

    @Update(entity = Transaction.class)
    void updateAll(Transaction... transactions);

    @Delete
    void delete(Transaction transaction);
}
