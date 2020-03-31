package com.wasteless.ui.add_transaction;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
    }

    public List<Wallet> getAllWallets(){
        return walletRepository.getAllWallets();
    }

    public List<String> getAllCategories(){
        return Arrays.asList(transactionRepository.getAllCategories());
    }

    //TODO: find the most optimal way to input wallet id
    public void insertExpense(String date, double amount, String description, Long walletId, boolean isIncome, String category, ArrayList<String> tags){
        Transaction newExpense = new Transaction(date, amount, description, walletId, isIncome, category);
        try{
            transactionRepository.insertExpense(newExpense, tags);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertIncome(String date, double amount, String description, Long walletId, boolean isIncome, String category, String source, ArrayList<String> tags){
        Transaction newIncome = new Transaction(date, amount, description, walletId, isIncome, category, source);
        try{
            transactionRepository.insertIncome(newIncome, tags);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
