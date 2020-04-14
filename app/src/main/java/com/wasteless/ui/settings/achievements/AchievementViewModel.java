package com.wasteless.ui.settings.achievements;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.wasteless.repository.AchievementRepository;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Achievement;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.add_transaction.AddTransactionViewModel;
import com.wasteless.ui.home.goal.GoalViewModel;
import com.wasteless.ui.settings.SettingsViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AchievementViewModel extends AndroidViewModel {
    private AchievementRepository achievementRepository;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    public MutableLiveData<Integer> currentlyDisplayWalletIndex;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AchievementViewModel(@NonNull Application application) {
        super(application);
        achievementRepository = AchievementRepository.getAchievementRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        walletRepository = walletRepository.getWalletRepository(application.getApplicationContext());
        currentlyDisplayWalletIndex = new MutableLiveData<>();
        currentlyDisplayWalletIndex.setValue(-1);
    }

    public double getTotalExpenseToday() {
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
    public double getTotalIncomeToday() {
        String today = dateFormatter.format(LocalDateTime.now());
        double todayTotalIncome = 0.0;
        if(currentlyDisplayWalletIndex.getValue() != -1){
            Wallet currentWallet = walletRepository.getAllWallets().get(currentlyDisplayWalletIndex.getValue());
            todayTotalIncome = transactionRepository.getTotalIncomeByDate(today, currentWallet.walletId);
        }else{
            todayTotalIncome = transactionRepository.getTotalIncomeByDate(today, Long.valueOf(-1));
        }
        return todayTotalIncome;
    }

    public List<Achievement> getAllAchievements(){
        return achievementRepository.getAllAchievements();
    }
    public void checkAllTheAchievements(){
        double todaysIncome = getTotalIncomeToday();
        double todaysExpense = getTotalExpenseToday();
        List<Achievement> allAchievements;
        allAchievements = getAllAchievements();
        if (!achievementRepository.getAchievementByName("Rich boy").isDone && todaysExpense > 5.0){
            achievementRepository.setAchievementToBeDone("Rich boy");
        }
        if (!achievementRepository.getAchievementByName("Poor guy").isDone && todaysIncome < 5.0){
            achievementRepository.setAchievementToBeDone("Poor guy");
        }

    }
}
