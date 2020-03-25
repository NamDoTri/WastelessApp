package com.wasteless.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasteless.repository.WalletRepository;

public class HomeViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private MutableLiveData<String> budgetAmount;
    private MutableLiveData<String> balanceAmount;
    private MutableLiveData<String> expensesAmount;
    private MutableLiveData<String> incomesAmount;

    public HomeViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());

        budgetAmount = new MutableLiveData<>();
        budgetAmount.setValue("234");

        balanceAmount = new MutableLiveData<>();
        balanceAmount.setValue(String.valueOf( walletRepository.getTotalBalance().getValue() ));

        expensesAmount = new MutableLiveData<>();
        expensesAmount.setValue("-23");

        incomesAmount = new MutableLiveData<>();
        incomesAmount.setValue("100");
    }

    public LiveData<String> getBudgetAmount() {
        return budgetAmount;
    }
    public LiveData<String> getBalanceAmount() {
        return balanceAmount;
    }
    public LiveData<String> getExpensesAmount() {
        return expensesAmount;
    }
    public LiveData<String> getIncomesAmount() {
        return incomesAmount;
    }
}