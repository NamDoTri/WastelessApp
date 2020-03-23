package com.wasteless.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionViewModel extends ViewModel{

    private MutableLiveData<String> mText;

    public TransactionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the transaction fragment. You will find info about transactions here.");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
