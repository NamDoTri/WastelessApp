package com.wasteless.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.models.TestTransaction;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<TestTransaction>> historyLiveData;
    private List<TestTransaction> historyArrayList = new ArrayList<>();

    public HistoryViewModel(){
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

    LiveData<List<TestTransaction>> getHistoryLiveData() {
        return historyLiveData;
    }

}
