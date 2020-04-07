package com.wasteless.roomdb.daos;

import com.wasteless.roomdb.entities.Wallet;

import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
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

    @Query("select coalesce(sum(balance), 0) from wallets")
    Double getTotalBalance();

    @Insert(entity = Wallet.class, onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Wallet... wallets);

    @Update(entity = Wallet.class, onConflict = OnConflictStrategy.IGNORE)
    void updateAll(Wallet... wallet);

    @Delete
    void delete(Wallet wallet);
}
