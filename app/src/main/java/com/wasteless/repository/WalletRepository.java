package com.wasteless.repository;

import android.content.Context;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletRepository {
    private static volatile WalletRepository instance = null;
    private final WalletDao walletDao;

    private WalletRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        walletDao = db.walletDao();
    }

    public static WalletRepository getWalletRepository(Context context){
        if(instance == null){
            instance = new WalletRepository(context);
        }
        return instance;
    }

    public List<Wallet> getAllWallets(){
        return walletDao.getAll();
    }

    public double getTotalBalance(){return walletDao.getTotalBalance();}

    public void insertWallet(Wallet wallet){
        walletDao.insertAll(wallet);
    }
}
