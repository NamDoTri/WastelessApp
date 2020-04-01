package com.wasteless.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;

import com.wasteless.roomdb.entities.Transaction;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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

    private MutableLiveData<Integer> currentlyDisplayWalletIndex;

    public HomeViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        currentlyDisplayWalletIndex = new MutableLiveData<>();
        currentlyDisplayWalletIndex.setValue(0);

        budgetAmount = new MutableLiveData<>();
        //TODO: set budget
        budgetAmount.setValue("234");

        balanceAmount = new MutableLiveData<>();
        balanceAmount.setValue(String.valueOf(walletRepository.getTotalBalance()));

        expensesAmount = new MutableLiveData<>();
        incomesAmount = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getCurrentlyDisplayWalletIndex() {return currentlyDisplayWalletIndex;}

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

    public LiveData<String> getTotalIncomeToday() {
        String today = dateFormatter.format(LocalDateTime.now());

        Double todayTotalIncome = transactionRepository.getTotalIncomeByDate(today);
        incomesAmount.setValue(String.valueOf(todayTotalIncome));
        return incomesAmount;
    }

    public String getTotalIncomeByMonth(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        double totalIncome = transactionRepository.getTotalIncomeByMonth(thisMonth);
        return String.valueOf(totalIncome);
    }

    public PieData getMonthlyIncomePieChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> incomesThisMonth = transactionRepository.getIncomesByMonth(thisMonth);

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

    public String getTotalExpensesByMonth(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        double totalExpenses = transactionRepository.getTotalExpenseByMonth(thisMonth);
        return String.valueOf(totalExpenses);
    }

    public PieData getMonthlyExpensePieChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> expensesThisMonth = transactionRepository.getExpensesByMonth(thisMonth);
        String[] expenseCategories = transactionRepository.getAllCategories();

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i=0; i<expenseCategories.length; i++){
            String expenseCategory = expenseCategories[i];

            double totalAmountPerType = expensesThisMonth.stream()
                    .filter(transaction -> transaction.type.equalsIgnoreCase(expenseCategory))
                    .mapToDouble(transaction -> transaction.amount)
                    .reduce(0, Double::sum);

            if(totalAmountPerType != 0.0)entries.add(new PieEntry((float) totalAmountPerType, expenseCategories[i]));
        }

        PieDataSet expensePieDataSet = new PieDataSet(entries, "");
        expensePieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(expensePieDataSet);
    }

    public BarData getExpenseBarChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> expensesThisMonth = transactionRepository.getExpensesByMonth(thisMonth);

        ArrayList expenseDays = new ArrayList();
        ArrayList allDays =  new ArrayList();
        ArrayList entries = new ArrayList<>();
        //ArrayList labels = new ArrayList<>();

        //Getting all days this month
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        //Add all days this month to the list
        for (int i=0; i<maxDay; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            allDays.add(simpleDateFormatter.format(calendar.getTime()));
        }

        //Add all days where there has been an expense to the list
        for (int i=0; i<expensesThisMonth.size(); i++){
            if(!expenseDays.contains(expensesThisMonth.get(i).date)){
                String date = expensesThisMonth.get(i).date;
                expenseDays.add(date);
            }
        }

        //Group expenses by date and add all dates with their corresponding expense amounts to the entries
        for (int i=0; i<allDays.size(); i++){
            String expenseDay = (String) allDays.get(i);

            double totalAmountPerDay = expensesThisMonth.stream()
                    .filter(transaction -> transaction.date.equalsIgnoreCase(expenseDay))
                    .mapToDouble(transaction -> transaction.amount)
                    .reduce(0, Double::sum);
            if(totalAmountPerDay == 0){
                entries.add(new BarEntry(i, 0));
            }
            else{
                entries.add(new BarEntry(i, (float) totalAmountPerDay));
            }
            //labels.add(expenseDay);
            //TODO: ^^ (adding date labels)
        }

        BarDataSet expenseBarDataSet = new BarDataSet(entries, "Total expenses per day");
        expenseBarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        //expenseBarDataSet.setDrawValues(false);
        //TODO: ^^ hide value "0" from the empty bars

        return new BarData(expenseBarDataSet);
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
}