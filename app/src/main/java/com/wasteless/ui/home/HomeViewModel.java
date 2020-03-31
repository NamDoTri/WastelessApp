package com.wasteless.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.PieEntry;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;

import com.wasteless.roomdb.entities.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.data.PieData;

public class HomeViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MutableLiveData<String> budgetAmount;
    private MutableLiveData<String> balanceAmount;
    private MutableLiveData<String> expensesAmount;
    private MutableLiveData<String> incomesAmount;

    private LiveData<List<Transaction>> expensesThisMonth;

    public HomeViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());

        budgetAmount = new MutableLiveData<>();
        //TODO: set budget
        budgetAmount.setValue("234");

        balanceAmount = new MutableLiveData<>();
        balanceAmount.setValue(String.valueOf(walletRepository.getTotalBalance()));

        expensesAmount = new MutableLiveData<>();
        incomesAmount = new MutableLiveData<>();

        this.getMonthIncomePieChart();
        this.getMonthlyExpenses();
    }

    public LiveData<String> getBudgetAmount() {
        return budgetAmount;
    }

    public LiveData<String> getBalanceAmount() {
        return balanceAmount;
    }

    public LiveData<String> getExpensesAmount() {
        String today = dateFormatter.format(LocalDateTime.now());

        Double todayTotalExpense = transactionRepository.getTotalExpenseByDate(today);
        expensesAmount.setValue(String.valueOf(todayTotalExpense));

        return expensesAmount;
    }

    public LiveData<String> getIncomesAmount() {
        String today = dateFormatter.format(LocalDateTime.now());

        Double todayTotalIncome = transactionRepository.getTotalIncomeByDate(today);
        incomesAmount.setValue(String.valueOf(todayTotalIncome));
        return incomesAmount;
    }

    public PieData getMonthIncomePieChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> incomesThisMonth = transactionRepository.getIncomesByMonth(thisMonth);
        Log.i("chart", String.valueOf(incomesThisMonth.size()));
//        ArrayList pieChartSegments = new ArrayList();

        return new PieData();
    }

    LiveData<List<Transaction>> getMonthlyExpenses(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        expensesThisMonth = transactionRepository.getExpensesByMonth(thisMonth);

        return expensesThisMonth;
    }
}