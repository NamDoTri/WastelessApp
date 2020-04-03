package com.wasteless.ui.transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel{

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private MutableLiveData<String> mText;

    public TransactionViewModel(Application application) {
        super(application);
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        mText = new MutableLiveData<>();
        mText.setValue("Transaction Details");
    }

    public Transaction getTransactionById(Long transactionId){
        return  transactionRepository.getTransactionById(transactionId);
    }

    public Wallet getWalletById(Long walletId){
        return walletRepository.getWalletById(walletId);
    }

    public void delete(Transaction transaction){
        transactionRepository.delete(transaction);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<String> getTags(Long transactionId){
        return transactionRepository.getTags(transactionId);
    }

}
