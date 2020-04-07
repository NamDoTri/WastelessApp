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

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AddTransactionViewModel extends AndroidViewModel {
    private AddTransactionViewModel instance = null;

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private Context appContext;
    private FirebaseVisionTextRecognizer textRecognizer;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<String> type = new MutableLiveData<>();
    private MutableLiveData<String> walletId = new MutableLiveData<>();
    private MutableLiveData<String> source = new MutableLiveData<>();

    private MutableLiveData<Boolean> isIncome = new MutableLiveData<>();;
    private MutableLiveData<ArrayList<String>> tags = new MutableLiveData<>();;

    public AddTransactionViewModel(Application application){
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
        appContext = application.getApplicationContext();

        if(description.getValue() == null){
            Log.i("receipt", "values are reset");
            description.setValue("");
            amount.setValue("0.0");
            date.setValue("");
            type.setValue("");
            walletId.setValue("");
            source.setValue("");
            isIncome.setValue(false);
            tags.setValue(new ArrayList<>());
        }

        // set up recognizer
        FirebaseApp.initializeApp(appContext);
        FirebaseVision instance = FirebaseVision.getInstance();
        textRecognizer = instance.getOnDeviceTextRecognizer();
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

    public MutableLiveData<String> recognizeText(Uri inputReceiptUri){
        MutableLiveData<String> resultText = new MutableLiveData<>();
        resultText.setValue("Text recognition is processing...");

        FirebaseVisionImage inputReceiptFVI;
        try{
            inputReceiptFVI = FirebaseVisionImage.fromFilePath(appContext, inputReceiptUri);
            textRecognizer.processImage(inputReceiptFVI)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText result) {
                            String extractedText = result.getText();
                            extractedText = extractedText.replaceAll("\n+", " "); // replace new line with whitespace
                            List<String> tokens = Arrays.asList(extractedText.split(" "));
                            // TODO: NLP generate tag

                            // TODO: NLP select category

                            // extracting data
                            Pattern dateFormat = Pattern.compile("(.*?)\\d\\d/\\d\\d/\\d\\d\\d\\d(.*?)|(.*?)\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d(.*?)|(.*?)\\d\\d-\\d\\d-\\d\\d\\d\\d(.*?)");
                            String amountFormat = "\\d+\\.\\d+|\\d+,\\d+";

                            for(String token : tokens){
                                try{
                                    if(dateFormat.matcher(token).find()){
                                        date.setValue(dateFormat.matcher(token).group(1));
                                    }else if(Pattern.matches(amountFormat, token)){
                                        String entry = token.replace(",", ".");
                                        if(Double.valueOf(entry) > Double.valueOf(amount.getValue())) amount.setValue(entry);
                                    }
                                }catch(Exception e){
                                    continue;
                                }
                            }
                            Log.i("receipt", "amount from vm: " + amount.getValue());
                                //TODO: train a model to extract total (if data is available)
                            resultText.setValue(result.getText());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseMLException mlException = (FirebaseMLException)e;
                            Log.i("receipt", "This comes from failure listener. Code: " + String.valueOf(mlException.getCode()));
                            if(mlException.getCode() == 14) { // model unavailable
                                //TODO
                                textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                                resultText.setValue("Text recognition model is downloading, please wait...");
                            }

                            e.printStackTrace();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultText;
    }
}
