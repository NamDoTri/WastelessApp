package com.wasteless.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.TransactionDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private static volatile TransactionRepository instance = null;
    private final TransactionDao transactionDao;
    private final WalletDao walletDao;

    private TransactionRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        transactionDao = db.transactionDao();
        walletDao = db.walletDao();
    }

    public static TransactionRepository getTransactionRepository(Context context){
        if(instance == null){
            instance = new TransactionRepository(context);
        }
        return instance;
    }

    public LiveData<List<Transaction>> getAllTransactions(){
        return transactionDao.getAllOrderByDate();
    }

    public LiveData<Double> getTotalExpenseByDate(String date){
        MutableLiveData<Double> totalExpense = new MutableLiveData<>();
        totalExpense.setValue(0.0);

        List<Transaction> expenses = transactionDao.getExpensesByDate(date);
        for(Transaction t : expenses){
            totalExpense.setValue( totalExpense.getValue() + t.amount );
        }

        return totalExpense;
    }

    public boolean insertExpense(Transaction transaction) throws Exception{
        if(transaction.isIncome == true) throw new Exception("Transaction is not an expense");

        try{
            try{
                transactionDao.insertAll(transaction);
            }catch(Exception e){
                e.printStackTrace();
            }

            // update wallet balance
            Wallet currentWallet = walletDao.getWalletById(transaction.wallet);
            currentWallet.balance -= transaction.amount;
            walletDao.updateAll(currentWallet);

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean insertIncome(Transaction transaction) throws Exception{
        if(transaction.isIncome != true) throw new Exception("Transaction is not an income");

        try{
            try{
                transactionDao.insertAll(transaction);
            }catch(Exception e){
                e.printStackTrace();
            }

            // update wallet balance
            Wallet currentWallet = walletDao.getWalletById(transaction.wallet);
            currentWallet.balance += transaction.amount;
            walletDao.updateAll(currentWallet);

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
