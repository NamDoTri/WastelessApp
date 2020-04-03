package com.wasteless.ui.add_transaction;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private Context appContext;

    private MutableLiveData<String> description, amount, date, type, walletId, source;
    private MutableLiveData<Boolean> isIncome;
    private MutableLiveData<ArrayList<String>> tags;

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        appContext = application.getApplicationContext();

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

    public List<String> getAllCategories(){return Arrays.asList(transactionRepository.getAllCategories());}
    public List<String> getAllIncomeTypes(){return Arrays.asList(transactionRepository.getAllIncomeTypes());}


    public boolean handleSubmitButtonPress(){
        boolean insertSuccess = true;

        String insertDescription = description.getValue();
        String insertAmount = amount.getValue();
        String insertDate = date.getValue();
        String insertWalletId = walletId.getValue();
        String insertType = type.getValue();
        ArrayList<String> insertTags = tags.getValue();
        String insertSource = source.getValue();

        Long id = null;
        List<Wallet> allWallets = this.getAllWallets();
        for (int i = 0; i < allWallets.size(); i++) {
            if (allWallets.get(i).name.contains(insertWalletId)){
                id = allWallets.get(i).walletId;
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

    public String recognizeText(Uri inputReceiptUri){
//        ImageDecoder.Source sourceContainer = ImageDecoder.createSource(appContext.getContentResolver(), inputReceiptUri);
//        Bitmap inputReceiptBitmap;
//        try{
//            inputReceiptBitmap = ImageDecoder.decodeBitmap(sourceContainer);
//            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
//            FirebaseVisionImage inputReceiptFVI = FirebaseVisionImage.fromBitmap(inputReceiptBitmap);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        // set up recognizer
        FirebaseApp.initializeApp(appContext);
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        String[] resultText = {"Something's wrong"};

        FirebaseVisionImage inputReceiptFVI;
        try{
            inputReceiptFVI = FirebaseVisionImage.fromFilePath(appContext, inputReceiptUri);
            textRecognizer.processImage(inputReceiptFVI)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText result) {
                            Log.i("receipt", "Result: " + result.getText());
                            resultText[0] = result.getText();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseMLException mlException = (FirebaseMLException)e;
                            Log.i("FA", "This comes from failure listener. Code: " + String.valueOf(mlException.getCode()));
                            if(mlException.getCode() == 14) { // model unavailable
                                //TODO
                            }

                            e.printStackTrace();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultText[0];
    }

}
