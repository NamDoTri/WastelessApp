package com.wasteless.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;

import com.wasteless.roomdb.entities.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.github.mikephil.charting.data.PieData;
import com.wasteless.roomdb.entities.Wallet;

public class HomeViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MutableLiveData<String> currentWalletName;
    private MutableLiveData<String> budgetAmount;
    private MutableLiveData<Integer> budgetProgress;
    private MutableLiveData<String> balanceAmount;
    private MutableLiveData<String> expensesAmount;
    private MutableLiveData<String> incomesAmount;
    private MutableLiveData<String> monthlyExpensesAmount;
    private MutableLiveData<String> monthlyIncomesAmount;

    public MutableLiveData<Integer> currentlyDisplayWalletIndex;

    public HomeViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        currentlyDisplayWalletIndex = new MutableLiveData<>();
        currentlyDisplayWalletIndex.setValue(-1); // -1 means all wallets

        currentWalletName = new MutableLiveData<>();
        currentWalletName.setValue("Overall");
        budgetAmount = new MutableLiveData<>();
        budgetAmount.setValue("0.0");
        budgetProgress = new MutableLiveData<>();
        budgetProgress.setValue(0);

        balanceAmount = new MutableLiveData<>();
        balanceAmount.setValue(String.valueOf(walletRepository.getTotalBalance()));

        expensesAmount = new MutableLiveData<>();
        incomesAmount = new MutableLiveData<>();
        monthlyExpensesAmount = new MutableLiveData<>();
        monthlyIncomesAmount= new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getCurrentlyDisplayWalletIndex() { return currentlyDisplayWalletIndex; }
    public MutableLiveData<String> getCurrentWalletName(){ return currentWalletName; }
    public LiveData<String> getBudgetAmount() {
        budgetAmount.setValue(String.valueOf(walletRepository.getMonthBudget()));
        return budgetAmount;
    }
    public LiveData<Integer> getBudgetProgress(){
        double currentProgress;
        try{
            currentProgress = Double.valueOf(monthlyExpensesAmount.getValue()) - Double.valueOf(monthlyIncomesAmount.getValue());
        }catch(Exception e){
            currentProgress = 0.0;
        }
        if(currentProgress > 0){
            int currentProgressInPercentage = (int)Math.round(currentProgress * 100 / Double.valueOf(budgetAmount.getValue()));
            budgetProgress.setValue(currentProgressInPercentage);
        }
        return budgetProgress;
    }
    public LiveData<String> getBalanceAmount() { return balanceAmount; }

    public LiveData<String> getTotalExpenseToday() {
        String today = dateFormatter.format(LocalDateTime.now());

        Double todayTotalExpense = 0.0;
        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, currentWallet.walletId);
        }else{
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, Long.valueOf(-1));
        }
        expensesAmount.setValue(String.valueOf(todayTotalExpense));

        return expensesAmount;
    }

    public String getTotalExpensesByMonth(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy

        double totalExpenses = 0.0;
        if(currentlyDisplayWalletIndex.getValue() == -1){
            totalExpenses = transactionRepository.getTotalExpensesByMonth(thisMonth);
        }else{
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            totalExpenses = transactionRepository.getExpensesByMonth(thisMonth).stream()
                    .filter(transaction -> transaction.wallet == currentWallet.walletId)
                    .mapToDouble(transaction -> transaction.amount)
                    .sum();
        }
        return String.valueOf(totalExpenses);
    }

    public LiveData<String> getLiveExpensesByMonth(){
        String totalExpenses = getTotalExpensesByMonth();
        monthlyExpensesAmount.setValue(String.valueOf(totalExpenses));

        return monthlyExpensesAmount;
    }

    public LiveData<String> getTotalIncomeToday() {
        String today = dateFormatter.format(LocalDateTime.now());

        double todayTotalIncome = 0.0;
        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            todayTotalIncome = transactionRepository.getTotalIncomeByDate(today, currentWallet.walletId);
        }else{
            todayTotalIncome = transactionRepository.getTotalIncomeByDate(today, Long.valueOf(-1));
        }
        incomesAmount.setValue(String.valueOf(todayTotalIncome));
        return incomesAmount;
    }
    public String getTotalExpenseTodayBar() {
        String today = dateFormatter.format(LocalDateTime.now());

        Double todayTotalExpense = 0.0;
        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, currentWallet.walletId);
        }else{
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, Long.valueOf(-1));
        }

        return String.valueOf(todayTotalExpense);
    }
    public String getTotalExpenseWeek() {


        Double todayTotalExpense = 0.0;
        for (int i = 1; i < 8; i++){
            LocalDate now = LocalDate.now();
            TemporalField fieldISO = WeekFields.of(Locale.GERMANY).dayOfWeek();
            String today = dateFormatter.format(now.with(fieldISO, i));
            if(currentlyDisplayWalletIndex.getValue() != -1){
                Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
                todayTotalExpense += transactionRepository.getTotalExpenseByDate(today, currentWallet.walletId);
                Log.i("progress", String.valueOf(todayTotalExpense));
            }else{
                todayTotalExpense += transactionRepository.getTotalExpenseByDate(today, Long.valueOf(-1));
                Log.i("progress", String.valueOf(todayTotalExpense));

            }

        }

        return String.valueOf(todayTotalExpense);
    }

    public String getTotalIncomeByMonth(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        double totalIncome = 0.0;
        if(currentlyDisplayWalletIndex.getValue() == -1){
            totalIncome = transactionRepository.getTotalIncomeByMonth(thisMonth);
        }else{
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            totalIncome = transactionRepository.getIncomesByMonth(thisMonth).stream()
                                                            .filter(transaction -> transaction.wallet == currentWallet.walletId)
                                                            .mapToDouble(transaction -> transaction.amount)
                                                            .sum();

        }
        return String.valueOf(totalIncome);
    }

    public LiveData<String> getLiveIncomesByMonth(){
        String monthTotalIncome = getTotalIncomeByMonth();
        monthlyIncomesAmount.setValue(monthTotalIncome);
        return monthlyIncomesAmount;
    }

    public PieData getMonthlyIncomePieChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> incomesThisMonth = transactionRepository.getIncomesByMonth(thisMonth);

        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            incomesThisMonth = incomesThisMonth.stream()
                                                .filter(transaction -> transaction.wallet == currentWallet.walletId)
                                                .collect(Collectors.toList());

        }

        String[] incomeTypes = transactionRepository.getAllIncomeTypes();

        ArrayList pieChartSegments = new ArrayList();

        for(int i = 0; i< incomeTypes.length; i++){
            String incomeType = incomeTypes[i];
            double totalIncomeOfThisType = incomesThisMonth.stream()
                                                        .filter(transaction -> transaction.type.equalsIgnoreCase(incomeType))
                                                        .mapToDouble(transaction -> transaction.amount)
                                                        .reduce(0, Double::sum);
            if(totalIncomeOfThisType != 0.0) pieChartSegments.add(new PieEntry((float)totalIncomeOfThisType, incomeType));
        }
        PieDataSet incomeDataSet = new PieDataSet(pieChartSegments, "");
        incomeDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        return new PieData(incomeDataSet);
    }

    public long getCurrentWalletId(){
            Wallet currentWallet;
        if(currentlyDisplayWalletIndex.getValue() != -1) {
            currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            return currentWallet.walletId;
        }
        else{
            return -1;
        }
    }

    public void changeWallet(String movement){
        int currentWallet = currentlyDisplayWalletIndex.getValue();

        if(movement.equalsIgnoreCase("prev")){
            currentWallet = currentWallet == -1 ? (walletRepository.getAllWallets().size() - 1) : (currentWallet - 1);
            currentlyDisplayWalletIndex.setValue(currentWallet);
        }
        else if(movement.equalsIgnoreCase("next")){
            currentWallet = (currentWallet == walletRepository.getAllWallets().size() - 1) ? -1 : (currentWallet + 1);
            currentlyDisplayWalletIndex.setValue(currentWallet);
        }
    }

    public void updateStats(){
        Wallet currentWallet;
        if(currentlyDisplayWalletIndex.getValue() != -1) {
            currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            currentWalletName.setValue(currentWallet.name);
            balanceAmount.setValue(String.valueOf(currentWallet.balance));
        }else {
            currentWalletName.setValue("Overall");
            balanceAmount.setValue(String.valueOf(walletRepository.getTotalBalance()));
        }
        getTotalIncomeToday();
        getTotalExpenseToday();
        getLiveExpensesByMonth();
        getLiveIncomesByMonth();
    }
}