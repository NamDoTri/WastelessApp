package com.wasteless.ui.history;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.models.TestTransaction;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;
    private LiveData<List<Transaction>> allTransactions;

    private MutableLiveData<List<TestTransaction>> historyLiveData;
    private List<TestTransaction> historyArrayList = new ArrayList<>();

    public HistoryViewModel(Application application){
        super(application);
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        allTransactions = transactionRepository.getAllTransactions();
        historyLiveData = new MutableLiveData<>();
        initViewModel();
    }

    private void initViewModel(){
        populateList();
        historyLiveData.setValue(historyArrayList);
    }

    private void populateList(){

        TestTransaction transaction = new TestTransaction("Test desc", "Test cat", "Test amount");
        historyArrayList.add(transaction);

        transaction = new TestTransaction("Test desc2", "Test cat2", "Test amount2");
        historyArrayList.add(transaction);

        transaction = new TestTransaction("Test desc3", "Test cat3", "Test amount3");
        historyArrayList.add(transaction);

    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    LiveData<List<TestTransaction>> getHistoryLiveData() {
        return historyLiveData;
    }

    LiveData<List<Transaction>> getAllTransactions(){
        return allTransactions;
    }
}
