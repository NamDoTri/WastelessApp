package com.wasteless.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.R;

public class TransactionViewModel extends ViewModel{

    private MutableLiveData<String> mText;

    public TransactionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Transaction Details");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
