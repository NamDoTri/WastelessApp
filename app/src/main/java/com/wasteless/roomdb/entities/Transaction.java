package com.wasteless.roomdb.entities;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(entity = Wallet.class,
                                parentColumns = "walletId",
                                childColumns = "wallet",
                                onDelete = ForeignKey.CASCADE))
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    public Long transactionId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    public String date;

    @ColumnInfo(name = "amount", defaultValue = "0")
    @NonNull
    public double amount;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "wallet")
    public Long wallet;

    @ColumnInfo(name = "isIncome")
    public boolean isIncome;

    @ColumnInfo(name = "type")
    public String type;

    // field of only Income
    @ColumnInfo(name = "source")
    public String source;

    //tags are stored in a separate table

    //constructor for expense
    public Transaction(String date, double amount, String description, Long wallet, boolean isIncome, String type){
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.wallet = wallet;
        this.isIncome = isIncome;
        this.type = type;
        this.source = null;
        Log.i("Database", "Transaction created");
    }

    //constructor for income
    @Ignore
    public Transaction(String date, double amount, String description, Long wallet, boolean isIncome, String type, String source){
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.wallet = wallet;
        this.isIncome = isIncome;
        this.type = type;
        this.source = source;
        Log.i("Database", "Transaction created: " + String.valueOf(this.transactionId));
    }

}
