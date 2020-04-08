package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
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

    @Query("select * from transactions where transactionId in (select transactionId from tag_assoc where tag = :tagName)")
    LiveData<List<Transaction>> getAllTransactionsByTagName(String tagName);

    @Query("select * from transactions where transactionId = :transactionId")
    Transaction getTransactionById(Long transactionId);

    @Query("select * from transactions where wallet = :walletId")
    List<Transaction> getTransactionsByWallet(Long walletId);

// QUERIES FOR SEARCH FRAGMENT
// Query to get transactions with a specific string somewhere in description
    @Query("SELECT * FROM transactions WHERE description LIKE  :searchValue")
    LiveData<List<Transaction>> getTransactionsByDescription(String searchValue);
//    Query to order transaction by description
    @Query("SELECT * FROM transactions ORDER BY description DESC")
    LiveData<List<Transaction>> orderTransactionsByDescription();

//    Query to get transaction with specific date
    @Query("SELECT * FROM transactions WHERE date LIKE :searchValue")
    LiveData<List<Transaction>> getTransactionsByDate(String searchValue);
//    Query to order transaction by date
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> orderTransactionsByDate();

//    Query to get all transactions with a type
    @Query("SELECT * FROM transactions WHERE type LIKE :searchValue")
    LiveData<List<Transaction>> getTransactionsByType(String searchValue);
//    Query to order transactions by type
    @Query("SELECT * FROM transactions ORDER BY type DESC")
    LiveData<List<Transaction>> orderTransactionsByType();

//    Query to get all transactions with a tag
    @Query("SELECT * FROM transactions WHERE transactionId IN (SELECT transactionId FROM tag_assoc WHERE tag LIKE :tagName)")
    LiveData<List<Transaction>> getTransactionsByTagName(String tagName);
//    Query to order transactions by tag
    @Query("SELECT * FROM transactions WHERE transactionId IN (SELECT transactionId FROM tag_assoc ORDER BY tag DESC)")
    LiveData<List<Transaction>> orderTransactionsByTagName();

// Query for getting the data to history view in order
    @Query("select * from transactions order by date desc")
    LiveData<List<Transaction>> getAllOrderByDate();

    // query incomes
    @Query("select * from transactions where isIncome = 1")
    List<Transaction> getAllIncomes();

    @Query("select * from transactions where isIncome = 1 and date like :date")
    List<Transaction> getIncomesByDate(String date);

    @Query("select coalesce(sum(amount), 0) from transactions where isIncome = 1 and date = :date")
    Double getTotalIncomeByDate(String date);

    @Query("select coalesce(sum(amount), 0) from transactions where isIncome = 1 and date like :month")
    Double getTotalIncomeByMonth(String month);

    @Query("select * from transactions where type = :type")
    List<Transaction> getIncomesByType(String type);

    @Query("select * from transactions where source like :source")
    List<Transaction> getIncomesBySource(String source);

    @Query("select * from transactions where wallet = :walletId and isIncome = 1")
    List<Transaction> getIncomesByWallet(Long walletId);

    @Query("select * from transactions where isIncome = 1 and date like :month")
    List<Transaction> getIncomesByMonth(String month);

    // query expenses
    @Query("select * from transactions where isIncome = 0")
    List<Transaction> getAllExpenses();

    @Query("select * from transactions where isIncome = 0 and date = :date")
    List<Transaction> getExpensesByDate(String date);

    @Query("select coalesce(sum(amount), 0) from transactions where isIncome = 0 and date = :date")
    Double getTotalExpenseByDate(String date);

    @Query("select coalesce(sum(amount), 0) from transactions where isIncome = 0 and date like :month")
    Double getTotalExpensesByMonth(String month);

    @Query("select * from transactions where type = :type")
    List<Transaction> getExpensesByCategory(String type);

    @Query("select * from transactions where wallet = :walletId and isIncome = 0")
    List<Transaction> getExpensesByWallet(Long walletId);

    @Query("select * from transactions where isIncome = 0 and date like :month order by date asc")
    List<Transaction> getExpensesByMonth(String month);

    //TODO: get incomes and expenses before and after a date

    @Insert(entity = Transaction.class, onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(Transaction... transactions);

    @Update(entity = Transaction.class, onConflict = OnConflictStrategy.IGNORE)
    int updateAll(Transaction... transactions);

    @Delete
    void delete(Transaction transaction);

}
