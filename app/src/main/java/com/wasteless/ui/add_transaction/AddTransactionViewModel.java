package com.wasteless.ui.add_transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.util.List;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
    }

    public List<Wallet> getAllWallets(){
        return walletRepository.getAllWallets();
    }

}
