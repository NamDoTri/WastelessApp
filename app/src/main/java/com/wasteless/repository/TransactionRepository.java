package com.wasteless.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.TagDao;
import com.wasteless.roomdb.daos.TransactionDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.TagAssociation;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private static volatile TransactionRepository instance = null;
    private final TransactionDao transactionDao;
    private final WalletDao walletDao;
    private final TagDao tagDao;

    private final String[] CATEGORIES = {"Groceries", "Entertainment", "Rent", "Commute"};
    private final String[] INCOMETYPES = {"Salary", "Gift", "Stolen"};

    private TransactionRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        transactionDao = db.transactionDao();
        walletDao = db.walletDao();
        tagDao = db.tagDao();
    }

    public static TransactionRepository getTransactionRepository(Context context){
        if(instance == null){
            instance = new TransactionRepository(context);
        }
        return instance;
    }

    public String[] getAllCategories(){
        return this.CATEGORIES;
    }

    public String[] getAllIncomeTypes(){
        return this.INCOMETYPES;
    }

//    SEARCH TRANSACTION METHODS
//    Get transactions by description
    public LiveData<List<Transaction>> getTransactionsByDescription( String description) {
        String adaptedString = "%" + description + "%";
        Log.d("TransactionRepository", "adaptedString: " + adaptedString);
        return transactionDao.getTransactionsByDescription(adaptedString);
    }
//    Get transactions by date
    public LiveData<List<Transaction>> getTransactionsByDate(String date) {
        String adaptedString = "%" + date + "%";
        return transactionDao.getTransactionsByDate(adaptedString);
    }
//    Get transactions by category
    public LiveData<List<Transaction>> getTransactionsByType(String category) {
        String adaptedString = "%" + category + "%";
        return transactionDao.getTransactionsByType(adaptedString);
    }
//    Get transactions by tags
    public LiveData<List<Transaction>> getTransactionsByTags(String tag) {
        String adaptedString = "%" + tag + "%";
        return transactionDao.getTransactionsByTagName(adaptedString);
    }

//    Get all transactions sorted in order
    public LiveData<List<Transaction>> getAllTransactions(){
        return transactionDao.getAllOrderByDate();
    }

    public Transaction getTransactionById(Long transactionId){
        return transactionDao.getTransactionById(transactionId);
    }

    //GET METHODS FOR MONTHLY INCOMES & EXPENSES
    public List<Transaction> getIncomesByMonth(String month){ // mm/yyyy
        return transactionDao.getIncomesByMonth("%" + month);
    }

    public double getTotalIncomeByMonth(String month){
        return transactionDao.getTotalIncomeByMonth("%" + month);
    }

    public double getTotalIncomeByDate(String date, Long walletId){
            return (walletId == -1) ?
                    transactionDao.getTotalIncomeByDate(date) :
                    transactionDao.getIncomesByDate(date).stream()
                                                        .filter(transaction -> transaction.wallet == walletId)
                                                        .mapToDouble(transaction -> transaction.amount)
                                                        .sum();
    }

    public List<Transaction> getExpensesByMonth(String month){ // mm/yyyy
        return transactionDao.getExpensesByMonth("%" + month);
    }

    public double getTotalExpensesByMonth(String month) {
        return transactionDao.getTotalExpensesByMonth("%" + month);
    }

    public double getTotalExpenseByDate(String date, Long walletId){
        return (walletId == -1) ?
                transactionDao.getTotalExpenseByDate(date) :
                transactionDao.getExpensesByDate(date).stream()
                                .filter(transaction -> transaction.wallet == walletId)
                                .mapToDouble(transaction -> transaction.amount)
                                .sum();
    }

    public boolean insertExpense(Transaction transaction, ArrayList<String> tags) throws Exception{
        if(transaction.isIncome) throw new Exception("Transaction is not an expense");
        try{
            try{
                //TODO: find a better solution to this because the parameter takes in only 1 transaction at a time
                List<Long> rows = transactionDao.insertAll(transaction);
                if(rows.size() > 0 && tags.size() > 0){
                    for(Long rowId : rows){
                        this.handleTags(rowId, tags);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            // update wallet balance
            Wallet currentWallet = walletDao.getWalletById(transaction.wallet);
            currentWallet.balance -= transaction.amount;
            walletDao.updateAll(currentWallet);

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertIncome(Transaction transaction, ArrayList<String> tags) throws Exception{
        if(!transaction.isIncome) throw new Exception("Transaction is not an income");

        try{
            try{
                List<Long> rows = transactionDao.insertAll(transaction);
                if(rows.size() > 0 && tags.size() > 0){
                    for(Long rowId : rows){
                        this.handleTags(rowId, tags);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            // update wallet balance
            Wallet currentWallet = walletDao.getWalletById(transaction.wallet);
            currentWallet.balance += transaction.amount;
            walletDao.updateAll(currentWallet);

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void handleTags(Long rowId, ArrayList<String> tags){
        ArrayList<Tag> tagsToInsert = new ArrayList<>();
        ArrayList<TagAssociation> tagAssociationsToInsert = new ArrayList<>();

        for(String tagName : tags){
            tagsToInsert.add(new Tag(tagName));
            tagAssociationsToInsert.add(new TagAssociation(rowId, tagName));
        }
        tagDao.insertAll(tagsToInsert.toArray(new Tag[tagsToInsert.size()]));
        tagDao.insertAllTagAssociation(tagAssociationsToInsert.toArray(new TagAssociation[tagAssociationsToInsert.size()]));
    }

    public void delete(Transaction transaction){
        transactionDao.delete(transaction);
    }

    public void update(Transaction transaction){
        transactionDao.updateAll(transaction);
    }

    public List<String> getTags(Long transactionId){
        return tagDao.getAllTagsOf(transactionId);
    }
}
