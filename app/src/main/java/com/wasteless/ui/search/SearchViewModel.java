package com.wasteless.ui.search;

import android.app.Application;
import android.widget.SearchView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {

//    SearchModel searchModel = new SearchModel();

    private TransactionRepository transactionRepository;

    private MutableLiveData<String> searchValue;
    private String activeFilter;
    private MutableLiveData<List<Transaction>> searchLiveData;
    private LiveData<List<Transaction>> onOpenData;
    private LiveData<List<Transaction>> onSearchData;
    private List<Transaction> testTransactionsList = new ArrayList<>();

    public SearchViewModel(Application application){
        super(application);
        searchLiveData = new MutableLiveData<>();
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        onOpenData = transactionRepository.getAllTransactions();
//        populateList();
        searchLiveData.setValue(testTransactionsList);
    }

    public void globalSearchHandler(String searchValue) {

    }

    public LiveData<List<Transaction>> getOnOpenData() {
        return onOpenData;
    }

//    Get transaction by date
    public LiveData<List<Transaction>> getDataByDate(String date) {
        return transactionRepository.getTransactionsByDate(date);
    }

//    Search transactions by a string in description
//    public LiveData<List<Transaction>> getSearchLiveData(String description) {
//        return transactionRepository.getTransactionsByDescription(description);
//    }

//    TODO add a search by description functionality here
    public LiveData<List<Transaction>> searchTransactionsByDescription(String  description) {
        return transactionRepository.getTransactionsByDescription(description);
    }

    public void setActiveFilter(String filter) {
        this.activeFilter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

    public String getActiveFilter() {
        return activeFilter;
    }


}