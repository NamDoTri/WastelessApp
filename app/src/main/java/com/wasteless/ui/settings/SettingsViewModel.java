package com.wasteless.ui.settings;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.MainActivity;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Wallet;

public class SettingsViewModel extends AndroidViewModel{
    private WalletRepository walletRepository;

    public SettingsViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
    }

    public void insertWallet(String name, double initialBalance, boolean isBank){
        walletRepository.insertWallet( new Wallet(name, initialBalance, isBank) );
    }
}
