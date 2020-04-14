package com.wasteless.ui.search;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.TagAssociation;
import com.wasteless.roomdb.entities.Transaction;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

//    SearchModel searchModel = new SearchModel();

    private TransactionRepository transactionRepository;

    private MutableLiveData<String> searchValue = new MutableLiveData<String>();
    private MutableLiveData<String> activeFilter = new MutableLiveData<String>();
    private MutableLiveData<List<Transaction>> searchLiveData;
    private LiveData<List<Transaction>> onOpenData;

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

    public List<TagAssociation> getTags() {
        return transactionRepository.getAllTags();
    }

//    Setters
    public LiveData<List<Transaction>> setActiveFilter(String filter) {
        activeFilter.setValue(filter);
        LiveData<List<Transaction>> transactions = globalSearchHandler();
        Log.d("search", "" + transactions);
        return transactions;
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
            if (currentSearchValue == "") {
                Log.d("SearchOutput", "SearchViewModel searchValue is empty " );
                return transactionRepository.orderTransactionsByDescription();
            }
            return transactionRepository.getTransactionsByDescription(currentSearchValue);
        }
//        Searches by tag if tags filter was chosen
        if ( currentActiveFilter == "tag" ) {
            if (currentSearchValue == "") {
                Log.d("SearchOutput", "SearchViewModel searchValue is empty " );
                return transactionRepository.orderTransactionsByTags();
            }
            return transactionRepository.getTransactionsByTags(currentSearchValue);
        }
//        Searches by date if date filter was chosen
        if ( currentActiveFilter == "date"  ) {
            Log.d("Search", "" + currentSearchValue);
            return transactionRepository.getTransactionsByDate(currentSearchValue);
        }
//        Searches by category/type if category filter was chose
        if ( currentActiveFilter == "category"  ) {
            if (currentSearchValue == "") {
                Log.d("SearchOutput", "SearchViewModel searchValue is empty " );
                return transactionRepository.orderTransactionsByType();
            }
            return transactionRepository.getTransactionsByType(currentSearchValue);
        } else {
            return null;
        }
    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public LiveData<List<Transaction>> getOnOpenData() {
        Log.d("start", "onOpenData: " + onOpenData);
        return onOpenData;
    }

//    Get transaction by date
//    public LiveData<List<Transaction>> getDataByDate(String date) {
//        return transactionRepository.getTransactionsByDate(date);
//    }
}