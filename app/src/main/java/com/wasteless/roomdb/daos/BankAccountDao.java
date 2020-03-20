package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.BankAccount;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankAccountDao {
    @Query("select * from bankaccount")
    List<BankAccount> getAll();

    @Query("select * from bankaccount where walletId in (:bankAccountId)")
    BankAccount getBankAccountById(int bankAccountId);

    @Query("select * from bankaccount where ibanNumber in (:ibanNumber)")
    BankAccount getBankAccountByIban(String ibanNumber);

    @Insert(entity = BankAccount.class)
    void insertAll(BankAccount... bankAccount);

    @Update(entity = BankAccount.class)
    void updateAll(BankAccount... bankAccount);

    @Delete
    void delete(BankAccount bankAccount);
}
