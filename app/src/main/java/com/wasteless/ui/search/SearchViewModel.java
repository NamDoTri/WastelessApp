package com.wasteless.ui.search;

import android.widget.SearchView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.models.TestTransaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    SearchModel searchModel = new SearchModel();

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> activeFilter;
    private MutableLiveData<List<TestTransaction>> searchLiveData;
    private List<TestTransaction> testTransactionsList = new ArrayList<>();

    public SearchViewModel(){
        searchLiveData = new MutableLiveData<>();
        populateList();
        searchLiveData.setValue(testTransactionsList);
    }


    private void populateList() {
        TestTransaction transaction = new TestTransaction("A box of milk", "Groceries", "1.80");
        testTransactionsList.add(transaction);

        transaction = new TestTransaction("Bread", "Groceries", "2.40");
        testTransactionsList.add(transaction);

        transaction = new TestTransaction("Cookies", "Groceries", "4.28");
        testTransactionsList.add(transaction);

        transaction = new TestTransaction("BigMac", "Cafes and Restaurants", "5.00");
        testTransactionsList.add(transaction);
    }

    public LiveData<List<TestTransaction>> getSearchLiveData() {
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