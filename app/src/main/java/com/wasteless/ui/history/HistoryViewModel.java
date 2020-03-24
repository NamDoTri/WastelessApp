package com.wasteless.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.models.TestTransaction;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    /*private MutableLiveData<String> mText;

    public HistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }*/

    private MutableLiveData<List<TestTransaction>> historyLiveData;
    private List<TestTransaction> historyArrayList = new ArrayList<>();

    public HistoryViewModel(){
       historyLiveData = new MutableLiveData<>();
       init();
    }

    public void init(){
        populateList();
        historyLiveData.setValue(historyArrayList);
    }

    public void populateList(){

        TestTransaction transaction = new TestTransaction("Test desc", "Test cat", "Test amount");
        historyArrayList.add(transaction);

        transaction = new TestTransaction("Test desc2", "Test cat2", "Test amount2");
        historyArrayList.add(transaction);

    }

    public LiveData<List<TestTransaction>> getHistoryLiveData() {
        return historyLiveData;
    }

}
