package com.wasteless.repository;

import android.content.Context;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.TransactionDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;

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

    public boolean insertExpense(Transaction transaction) throws Exception{
        if(transaction.isIncome == true){
            throw new Exception("Transaction is not an expense");
        }
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
}
