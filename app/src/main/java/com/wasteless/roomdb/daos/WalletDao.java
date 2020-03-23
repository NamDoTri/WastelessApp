package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Wallet;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WalletDao {
    @Query("select * from wallets")
    List<Wallet> getAll();

    @Query("select * from wallets where walletId = :walletId")
    Wallet getWalletById(Long walletId);

    @Insert(entity = Wallet.class)
    void insertAll(Wallet... wallets);

    @Update(entity = Wallet.class)
    void updateAll(Wallet... wallet);

    @Delete
    void delete(Wallet wallet);

    //TODO: remove all incomes & expenses when deleting wallet

}
