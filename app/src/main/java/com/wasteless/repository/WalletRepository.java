package com.wasteless.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletRepository {
    private static WalletRepository instance = null;
    private final WalletDao walletDao;
    private MutableLiveData<Double> totalBalance = new MutableLiveData<>();

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

    public MutableLiveData<Double> getTotalBalance(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<Wallet> wallets = walletDao.getAll();
//                double balance = totalBalance.getValue();
//                for(int i = 0; i < wallets.size(); i++){
//                    balance += wallets.get(i).balance;
//                }
//                totalBalance.setValue(balance);
//            }
//        }).start();
        final List<Wallet> wallets = walletDao.getAll();
        double balance = totalBalance.getValue() == null ? 0 : totalBalance.getValue() ;
        for(int i = 0; i < wallets.size(); i++){
            balance += wallets.get(i).balance;
        }
        totalBalance.setValue(balance);
        return totalBalance;
    }

}