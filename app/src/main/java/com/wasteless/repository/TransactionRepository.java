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

//    SEARCH TRANSACTION METHODS
    public LiveData<List<Transaction>> getTransactionsByDescription(String description) {
        String adaptedString = "%" + description + "%";
        return transactionDao.getTransactionsByDescription(adaptedString);
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

    public Transaction getTransactionById(Long transactionId){
        return transactionDao.getTransactionById(transactionId);
    }

    public double getTotalExpenseByDate(String date){
        return transactionDao.getTotalExpenseByDate(date);
    }

    public double getTotalIncomeByDate(String date){
        return transactionDao.getTotalIncomeByDate(date);
    }

    public boolean insertExpense(Transaction transaction, ArrayList<String> tags) throws Exception{
        if(transaction.isIncome) throw new Exception("Transaction is not an expense");

        try{
            try{
                //TODO: find a better solution to this because the parameter takes in only 1 transaction at a time
                List<Long> rows = transactionDao.insertAll(transaction);
                if(rows.size() > 0 && tags.size() > 0){
                    for(Long rowId : rows){
                        this.handleTags(rowId, tags);
                    }
                }
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
    
    public boolean insertIncome(Transaction transaction, ArrayList<String> tags) throws Exception{
        if(!transaction.isIncome) throw new Exception("Transaction is not an income");

        try{
            try{
                List<Long> rows = transactionDao.insertAll(transaction);
                if(rows.size() > 0 && tags.size() > 0){
                    for(Long rowId : rows){
                        this.handleTags(rowId, tags);
                    }
                }
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

    private void handleTags(Long rowId, ArrayList<String> tags){
        //insert to tag table
        ArrayList<Tag> tagsToInsert = new ArrayList<>();
        for(String tagName : tags){
            tagsToInsert.add(new Tag(tagName));

            //insert to tag_assoc
            tagDao.insertTagAssociation(rowId, tagName);
        }
        tagDao.insertAll(tagsToInsert.toArray(new Tag[tagsToInsert.size()] ));
    }

    public void delete(Transaction transaction){
        transactionDao.delete(transaction);
    }

    public void update(Transaction transaction){
        transactionDao.updateAll(transaction);
    }

}
