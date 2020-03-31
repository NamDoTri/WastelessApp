package com.wasteless.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;

import com.wasteless.roomdb.entities.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

public class HomeViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MutableLiveData<String> budgetAmount;
    private MutableLiveData<String> balanceAmount;
    private MutableLiveData<String> expensesAmount;
    private MutableLiveData<String> incomesAmount;

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

        String[] incomeTypes = transactionRepository.getAllCategories(); //TODO: change it to income type after refactoring add transaction viewmodel

        ArrayList pieChartSegments = new ArrayList();

        for(int i = 0; i< incomeTypes.length; i++){
            String incomeType = incomeTypes[i];
            double totalIncomeOfThisType = incomesThisMonth.stream()
                                                        .filter(transaction -> transaction.type.equalsIgnoreCase(incomeType))
                                                        .mapToDouble(transaction -> transaction.amount)
                                                        .reduce(0, Double::sum);
            pieChartSegments.add(new PieEntry((float)totalIncomeOfThisType, (float)i));
        }
        PieDataSet incomeDataSet = new PieDataSet(pieChartSegments, "Income this month");
        return new PieData(incomeDataSet);
    }
}