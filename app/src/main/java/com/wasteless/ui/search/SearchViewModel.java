package com.wasteless.ui.search;

import android.app.Application;
import android.widget.SearchView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.models.TestTransaction;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {

//    SearchModel searchModel = new SearchModel();

    private TransactionRepository transactionRepository;

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> activeFilter;
    private MutableLiveData<List<Transaction>> searchLiveData;
    private LiveData<List<Transaction>> onOpenData;
    private List<Transaction> testTransactionsList = new ArrayList<>();

    public SearchViewModel(Application application){
        super(application);
        searchLiveData = new MutableLiveData<>();
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        onOpenData = transactionRepository.getAllTransactions();
//        populateList();
        searchLiveData.setValue(testTransactionsList);
    }

//    private void populateList() {
//        Transaction transaction = new Transaction("A box of milk", "Groceries", "1.80");
//        testTransactionsList.add(transaction);
//
//        transaction = new Transaction("Bread", "Groceries", "2.40");
//        testTransactionsList.add(transaction);
//
//        transaction = new Transaction("Cookies", "Groceries", "4.28");
//        testTransactionsList.add(transaction);
//
//        transaction = new Transaction("BigMac", "Cafes and Restaurants", "5.00");
//        testTransactionsList.add(transaction);
//    }

    public LiveData<List<Transaction>> getOnOpenData() {
        return onOpenData;
    }

    public LiveData<List<Transaction>> getSearchLiveData() {
        return searchLiveData;
    }

    public void setActiveFilter(MutableLiveData<String> filter) {
        this.activeFilter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

    public MutableLiveData<String> getActiveFilter() {
        return activeFilter;
    }


}