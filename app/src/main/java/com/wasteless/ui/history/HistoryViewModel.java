package com.wasteless.ui.history;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;
    private LiveData<List<Transaction>> allTransactions;

    public HistoryViewModel(Application application){
        super(application);
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        allTransactions = transactionRepository.getAllTransactions();
    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    LiveData<List<Transaction>> getAllTransactions(){
        return allTransactions;
    }
}
