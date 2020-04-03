package com.wasteless.ui.home.expenses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ExpensesViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    public MutableLiveData<Integer> currentlyDisplayWalletIndex;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    HomeViewModel homeViewModel;

    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
    }

    public PieData getMonthlyExpensePieChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> expensesThisMonth = transactionRepository.getExpensesByMonth(thisMonth);
        String[] expenseCategories = transactionRepository.getAllCategories();

        ArrayList<PieEntry> entries = new ArrayList<>();

        if(1 != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(1);
            expensesThisMonth = expensesThisMonth.stream()
                    .filter(transaction -> transaction.wallet == currentWallet.walletId)
                    .collect(Collectors.toList());
        }

        for (int i=0; i<expenseCategories.length; i++){
            String expenseCategory = expenseCategories[i];

            double totalAmountPerType = expensesThisMonth.stream()
                    .filter(transaction -> transaction.type.equalsIgnoreCase(expenseCategory))
                    .mapToDouble(transaction -> transaction.amount)
                    .reduce(0, Double::sum);

            if(totalAmountPerType != 0.0)entries.add(new PieEntry((float) totalAmountPerType, expenseCategories[i]));
        }

        PieDataSet expensePieDataSet = new PieDataSet(entries, "");
        expensePieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        expensePieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(expensePieDataSet);
    }

    public BarData getExpenseBarChart(){
        String thisMonth = dateFormatter.format(LocalDateTime.now()).substring(3); // mm/yyyy
        List<Transaction> expensesThisMonth = transactionRepository.getExpensesByMonth(thisMonth);

        ArrayList expenseDays = new ArrayList();
        ArrayList allDays =  new ArrayList();
        ArrayList entries = new ArrayList<>();

        //Getting all days this month
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        if(1 != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(1);
            expensesThisMonth = expensesThisMonth.stream()
                    .filter(transaction -> transaction.wallet == currentWallet.walletId)
                    .collect(Collectors.toList());
        }

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
        for (int i=0; i<allDays.size(); i++) {
            String expenseDay = (String) allDays.get(i);

            double totalAmountPerDay = expensesThisMonth.stream()
                    .filter(transaction -> transaction.date.equalsIgnoreCase(expenseDay))
                    .mapToDouble(transaction -> transaction.amount)
                    .reduce(0, Double::sum);
            if (totalAmountPerDay == 0) {
                entries.add(new BarEntry(i, 0, "hide"));
            } else {
                entries.add(new BarEntry(i, (float) totalAmountPerDay));
            }
        }
        BarDataSet expenseBarDataSet = new BarDataSet(entries, "Total expenses per day");
        expenseBarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        return new BarData(expenseBarDataSet);
    }

    public ArrayList<String> getDateLabels() {
        ArrayList allDates = new ArrayList();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd");

        for (int i=0; i<maxDay; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            allDates.add(simpleDateFormatter.format(calendar.getTime()));
        }

        return allDates;
    }
}
