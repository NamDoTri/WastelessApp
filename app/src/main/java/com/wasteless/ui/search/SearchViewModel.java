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

    private MutableLiveData<String> searchValue = new MutableLiveData<String>();
    private MutableLiveData<String> activeFilter = new MutableLiveData<String>();
    private MutableLiveData<List<Transaction>> searchLiveData;
    private LiveData<List<Transaction>> onOpenData;
    private LiveData<List<Transaction>> onSearchData;
    private List<Transaction> testTransactionsList = new ArrayList<>();

    public SearchViewModel(Application application){
        super(application);
        searchLiveData = new MutableLiveData<>();
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        onOpenData = transactionRepository.getTransactionsByFilterAndDescription(activeFilter.toString(), searchValue.toString());
    }

//    Getters
    public MutableLiveData<String> getActiveFilter() {
    return activeFilter;
}

//    Setters
    public void setActiveFilter(String filter) {
        activeFilter.setValue(filter);
        globalSearchHandler();
    }

    public void setSearchValue(String searchV) {
        searchValue.setValue(searchV);
        globalSearchHandler();
    }

//    Global search handler that assigns the values based on the filter and search values
    public void globalSearchHandler() {
//        if ( activeFilter ) {
//
//        }
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

//    searchValue

//    TODO add a search by description functionality here
//    public LiveData<List<Transaction>> searchTransactionsByDescription(searchValue) {
//        return transactionRepository.getTransactionsByDescription(description);
//    }
}