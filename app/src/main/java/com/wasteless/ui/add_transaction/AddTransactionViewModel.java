package com.wasteless.ui.add_transaction;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.repository.WalletRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTransactionViewModel extends AndroidViewModel {
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private Context appContext;
    private FirebaseVisionTextRecognizer textRecognizer;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean isTranslateModelAvailable = false;

    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private MutableLiveData<String> type = new MutableLiveData<>();
    private MutableLiveData<String> walletId = new MutableLiveData<>();
    private MutableLiveData<String> source = new MutableLiveData<>();
    private MutableLiveData<Boolean> isIncome = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> tags = new MutableLiveData<>();

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

        // check for available translation model
        FirebaseModelManager modelManager = FirebaseModelManager.getInstance();
        modelManager.getDownloadedModels(FirebaseTranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> firebaseTranslateRemoteModels) {
                        for(FirebaseTranslateRemoteModel model : firebaseTranslateRemoteModels){
                            if(model.getLanguageCode().equalsIgnoreCase("fi")) isTranslateModelAvailable = true;
                        }
                        if(!isTranslateModelAvailable){
                            //download Finnish translate model
                            FirebaseTranslateRemoteModel fiModel = new FirebaseTranslateRemoteModel().Builder(FirebaseTranslateLanguage.FI).build();
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions().Builder().requireWifi().build();
                            modelManager.download(fiModel, conditions)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            isTranslateModelAvailable = true;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                        }
                                    })
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
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
            extractContent(inputReceiptFVI);
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultText;
    }

    public MutableLiveData<String> recognizeText(Bitmap inputreceiptBitmap){
        MutableLiveData<String> resultText = new MutableLiveData<>();
        resultText.setValue("Text recognition is processing...");

        FirebaseVisionImage inputReceiptFVI;
        try{
            inputReceiptFVI = FirebaseVisionImage.fromBitmap(inputreceiptBitmap);
            extractContent(inputReceiptFVI);
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultText;
    }

    private void extractContent(FirebaseVisionImage inputReceiptFVI){
        textRecognizer.processImage(inputReceiptFVI)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        final String extractedText = result.getText();

                        // identify language
                        FirebaseNaturalLanguage.getInstance()
                                .getLanguageIdentification()
                                .identifyLanguage(extractedText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        if(!s.equalsIgnoreCase("fi")){
                                            // unsupported language notification
                                        }
                                        else if(!isTranslateModelAvailable){
                                            // unavailable translate model noti
                                        }
                                        else{
                                            // translate FI -> EN
                                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions().Builder()
                                                    .setSourceLanguage(FirebaseTranslateLanguage.FI)
                                                    .setTargetLanguage(FirebaseTranslateLanguage.EN)
                                                    .build();
                                            FirebaseTranslator finEngTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

                                            finEngTranslator.translate(extractedText)
                                                    .addOnSuccessListener(new OnSuccessListener<String>(){
                                                        @Override
                                                        public void onSuccess(@NonNull String translatedText){
                                                            Log.i("Receipt translate", "Translated text: " + translatedText);
                                                            String processedText = translatedText.replaceAll("\n+", " "); // replace new line with whitespace
                                                            // TODO: NLP generate tag

                                                            // TODO: NLP select category

                                                            // extracting data
                                                            extractDateAndAmount(processedText);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener(){
                                                        @Override
                                                        public void onFailure(@NonNull Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    })
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> Log.i("receipt translate", "Model downloading..."));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseMLException mlException = (FirebaseMLException)e;
                        Log.i("receipt", "This comes from failure listener. Code: " + String.valueOf(mlException.getCode()));
                        if(mlException.getCode() == 14) { // model unavailable
                            textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                        }
                        e.printStackTrace(); //TODO: remove
                    }
                });
    }

    private void extractDateAndAmount(String extractedText){
        Pattern dateFormat = Pattern.compile("(.*?)\\d\\d/\\d\\d/\\d\\d\\d\\d(.*?)|(.*?)\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d(.*?)|(.*?)\\d\\d-\\d\\d-\\d\\d\\d\\d(.*?)");
        String amountFormat = "(.*?)\\d+\\.\\d+|\\d+,\\d+(.*?)"; // doesnt match currency
        List<String> tokens = Arrays.asList(extractedText.split(" "));

        for(String token : tokens){
            try{
                Matcher matcher = dateFormat.matcher(token);
                if(matcher.find()){
                    date.setValue(matcher.group());
                }else if(Pattern.matches(amountFormat, token)){
                    String entry = token.replace(",", ".");
                    if(Double.valueOf(entry) > Double.valueOf(amount.getValue())) amount.setValue(entry);
                }
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
        //TODO: train a model to extract total (if data is available)
    }
}
