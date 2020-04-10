package com.wasteless.ui.home.goal;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.repository.GoalRepository;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Goal;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.home.HomeViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    public MutableLiveData<Integer> currentlyDisplayWalletIndex;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    public GoalViewModel(Application application){
        super(application);
        goalRepository = goalRepository.getGoalRepository(application.getApplicationContext());
        transactionRepository = transactionRepository.getTransactionRepository(application.getApplicationContext());
        walletRepository = walletRepository.getWalletRepository(application.getApplicationContext());
        currentlyDisplayWalletIndex = new MutableLiveData<>();
        currentlyDisplayWalletIndex.setValue(-1);
    }

    public double getTotalExpenseTodayBar() {
        String today = dateFormatter.format(LocalDateTime.now());
        Double todayTotalExpense = 0.0;
        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, currentWallet.walletId);
        }else{
            todayTotalExpense = transactionRepository.getTotalExpenseByDate(today, Long.valueOf(-1));
        }
        return todayTotalExpense;
    }

    public void insertGoalByType(String date, double goal, String type){
        Goal newGoal = new Goal(type, date, goal);
        try{
            goalRepository.insertGoal(newGoal);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteGoalsByType(String type){
        try{
            goalRepository.deleteGoalByType(type);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Goal getGoalByType(String type){
        Goal dailyGoal = null;
        try{
            dailyGoal = goalRepository.getGoalByType(type);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dailyGoal;
    }
    public String getDayProgress() {
        Goal dailyGoal = null;
        double progress = 0.0;
        try{
            dailyGoal = goalRepository.getGoalByType("daily");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(dailyGoal != null){
             progress =  getTotalExpenseTodayBar() * 100 / dailyGoal.amountOfMoney;
            Log.i("important", String.valueOf(progress));
        }
        return String.valueOf(progress);
    }
}
