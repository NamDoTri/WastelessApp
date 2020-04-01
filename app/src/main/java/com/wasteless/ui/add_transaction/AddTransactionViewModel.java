package com.wasteless.ui.add_transaction;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.wasteless.R;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    private MutableLiveData<String> description, amount, date, type, walletId, source;
    private MutableLiveData<Boolean> isIncome;
    private MutableLiveData<ArrayList<String>> tags;

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());

        description = new MutableLiveData<>();
        amount = new MutableLiveData<>();
        date = new MutableLiveData<>();
        type = new MutableLiveData<>();
        walletId = new MutableLiveData<>();
        source = new MutableLiveData<>();
        isIncome = new MutableLiveData<>();
        tags = new MutableLiveData<>();

        description.setValue("");
        amount.setValue("");
        date.setValue("");
        type.setValue("");
        walletId.setValue("");
        source.setValue("");
        isIncome.setValue(false);
        tags.setValue(new ArrayList<>());
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<String> getAmount() {
        return amount;
    }

    public MutableLiveData<String> getDate() {
        return date;
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public MutableLiveData<String> getWalletId() {
        return walletId;
    }

    public MutableLiveData<String> getSource() {
        return source;
    }

    public MutableLiveData<Boolean> getIsIncome() {
        return isIncome;
    }

    public MutableLiveData<ArrayList<String>> getTags() {return tags;}

    public List<Wallet> getAllWallets(){
        return walletRepository.getAllWallets();
    }

    public List<String> getAllCategories(){
        return Arrays.asList(transactionRepository.getAllCategories());
    }

    public boolean handleSubmitButtonPress(){
        boolean insertSuccess = true;

        String insertDescription = description.getValue();
        String insertAmount = amount.getValue();
        String insertDate = date.getValue();
        String insertWalletId = walletId.getValue();
        String insertType = type.getValue();
        ArrayList<String> insertTags = tags.getValue();
        String insertSource = source.getValue();

//        String date        = ((EditText)root.findViewById(R.id.date)).getText().toString();
//        String category    = ((Spinner)root.findViewById(R.id.category)).getSelectedItem().toString();
//        String sum         = ((EditText)root.findViewById(R.id.sum)).getText().toString().trim();
//        String description = ((EditText)root.findViewById(R.id.description)).getText().toString();
//        String source      = ((EditText)root.findViewById(R.id.source)).getText().toString();
//        String wallet      = ((Spinner)root.findViewById(R.id.wallet)).getSelectedItem().toString();
//
//        //Validation of all the input fields
//        if(date.trim().length() > 0 &&
//                category.trim().length() > 0 &&
//                wallet.trim().length() > 0 &&
//                sum.trim().length() > 0 &&
//                description.trim().length() > 0) {
//
//            if (isIncome == false) {
//                addTransactionViewModel.insertExpense(date, Float.parseFloat(sum), description, id, false, category, tags);
//                successMessage();
//            } else {
//                if (source.trim().length() <= 0) {
//                    failedMessage();
//                } else {
//                    addTransactionViewModel.insertIncome(date, Float.parseFloat(sum), description, id, true, category, source, tags);
//                    successMessage();
//                }
//            }
//        } else {
//            failedMessage();
//            Log.i("transaction", "something is missed");
//        };

        Long id = null;
        List<Wallet> allWallets = this.getAllWallets();
        for (int i = 0; i < allWallets.size(); i++) {
            if (allWallets.get(i).name.contains(insertWalletId)){
                id = allWallets.get(i).walletId;
                Log.i("id", String.valueOf(id));
            }
        }

        if(this.transactionIsValid(insertDescription, insertAmount, insertDate, insertWalletId, insertType, insertSource)){
            if(isIncome.getValue()){
                insertIncome(insertDate, Double.valueOf(insertAmount), insertDescription, id, isIncome.getValue(), insertType, insertSource, insertTags);
            }else{
                insertExpense(insertDate, Double.valueOf(insertAmount), insertDescription, id, isIncome.getValue(), insertType, insertTags);
            }
        }else{
            insertSuccess = false;
        }
        return insertSuccess;
    }

    //TODO: find the most optimal way to input wallet id
    public void insertExpense(String date, double amount, String description, Long walletId, boolean isIncome, String category, ArrayList<String> tags){
        Transaction newExpense = new Transaction(date, amount, description, walletId, isIncome, category);
        try{
            transactionRepository.insertExpense(newExpense, tags);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertIncome(String date, double amount, String description, Long walletId, boolean isIncome, String category, String source, ArrayList<String> tags){
        Transaction newIncome = new Transaction(date, amount, description, walletId, isIncome, category, source);
        try{
            transactionRepository.insertIncome(newIncome, tags);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean transactionIsValid(String description, String amount, String date, String walletId, String type, String source){
        if(description.length() == 0 ||
                amount.length() == 0 ||
                date.length() == 0 ||
                walletId.length() == 0 ||
                type.length() == 0){
            return false;
        }
        if(isIncome.getValue() && source.length() == 0) return false;
        return true;
    }

}
