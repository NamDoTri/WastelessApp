package com.wasteless.repository;

import android.content.Context;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.BudgetDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Budget;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletRepository {
    private static volatile WalletRepository instance = null;
    private final WalletDao walletDao;
    private final BudgetDao budgetDao;

    private WalletRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        walletDao = db.walletDao();
        budgetDao = db.budgetDao();
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

    public void UpdateWallet(Wallet wallet){
        walletDao.updateAll(wallet);
    }


    public Wallet getWalletByName(String name){ return  walletDao.getWalletByName(name);}


    public Wallet getWalletById(Long walletId){
        return walletDao.getWalletById(walletId);
    }

    public double getMonthBudget(){
        return budgetDao.getMonthBudget() == null ?
                0.0 :
                budgetDao.getMonthBudget();
    }
    public void insertMonthBudget(double amount, String createdAt){
        Budget newBudget = new Budget(amount, createdAt, true); //TODO
        budgetDao.insert(newBudget);
    }
}
