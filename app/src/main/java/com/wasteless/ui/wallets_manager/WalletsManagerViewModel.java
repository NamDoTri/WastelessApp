package com.wasteless.ui.wallets_manager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class WalletsManagerViewModel extends AndroidViewModel {

    WalletRepository walletRepository;

    public WalletsManagerViewModel(Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());;

    }


    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = walletRepository.getAllWallets();
        Log.d("wallets", "view model: " + wallets);
        return  wallets;
    }

}
