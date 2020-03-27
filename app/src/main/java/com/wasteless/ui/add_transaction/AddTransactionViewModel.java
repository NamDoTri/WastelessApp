package com.wasteless.ui.add_transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.util.Arrays;
import java.util.List;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private final String[] CATEGORIES = {"Groceries", "Entertainment", "Rent", "Commute"};

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
    }

    public List<Wallet> getAllWallets(){
        return walletRepository.getAllWallets();
    }

    public List<String> getAllCategories(){
        return Arrays.asList(CATEGORIES);
    }
}
