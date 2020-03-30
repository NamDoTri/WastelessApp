package com.wasteless.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.TagDao;
import com.wasteless.roomdb.daos.TransactionDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionRepository {
    private static volatile TransactionRepository instance = null;
    private final TransactionDao transactionDao;
    private final WalletDao walletDao;
    private final TagDao tagDao;

    private TransactionRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        transactionDao = db.transactionDao();
        walletDao = db.walletDao();
        tagDao = db.tagDao();
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

    public double getTotalExpenseByDate(String date){
        return transactionDao.getTotalExpenseByDate(date);
    }

    public double getTotalIncomeByDate(String date){
        return transactionDao.getTotalIncomeByDate(date);
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
    public void handleTags(ArrayList<String> tags){
        //convert ArrayList<String> to ArrayList<Tag>
        ArrayList<Tag> toInsert = new ArrayList<>();
        for(String tagName : tags){
            toInsert.add(new Tag(tagName));
        }
        //insert to db
        List<Long> rows = tagDao.insertAll(toInsert.toArray(new Tag[toInsert.size()] ));
        Log.i("tag", String.valueOf(rows));
    }
}
