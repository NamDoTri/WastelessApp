package com.wasteless.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel{
    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is settings fragment. I'm going to use preferenceFragment and it takes time to implement it");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
