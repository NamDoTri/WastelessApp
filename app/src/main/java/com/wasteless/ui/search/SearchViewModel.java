package com.wasteless.ui.search;

import android.app.Application;
import android.util.Log;
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
        onOpenData = transactionRepository.getAllTransactions();
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

    public LiveData<List<Transaction>> setSearchValue(String searchV) {
        searchValue.setValue(searchV);
        LiveData<List<Transaction>> transactions = globalSearchHandler();
        return transactions;
    }

//    Global search handler that assigns the values based on the filter and search values
    public LiveData<List<Transaction>> globalSearchHandler() {
        Log.d("SearchOutput", "SearchViewModel activeFilter : " + activeFilter.getValue());
        Log.d("SearchOutput", "SearchViewModel searchValue : " + searchValue.getValue());
        String currentActiveFilter = activeFilter.getValue();
        String currentSearchValue = searchValue.getValue();
//        Searches by description if description filter was chosen
        if ( currentActiveFilter == "description" ) {
            return transactionRepository.getTransactionsByDescription(currentSearchValue);
        }
//        Searches by tag if tags filter was chosen
        if ( currentActiveFilter == "tag" ) {
            return transactionRepository.getTransactionsByTags(currentSearchValue);
        }
//        Searches by date if date filter was chosen
        if ( currentActiveFilter == "date"  ) {
            return transactionRepository.getTransactionsByDate(currentSearchValue);
        }
//        Searches by category/type if category filter was chose
        if ( currentActiveFilter == "category"  ) {
            return transactionRepository.getTransactionsByType(currentSearchValue);
        } else {
            return null;
        }
    }

    public LiveData<List<Transaction>> getOnOpenData() {
        Log.d("start", "onOpenData: " + onOpenData);
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