package com.wasteless.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> budgetAmount;
    private MutableLiveData<String> balanceAmount;
    private MutableLiveData<String> expensesAmount;
    private MutableLiveData<String> incomesAmount;

    public HomeViewModel() {
        budgetAmount = new MutableLiveData<>();
        budgetAmount.setValue("234");

        balanceAmount = new MutableLiveData<>();
        balanceAmount.setValue("23434");

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