package com.wasteless.roomdb.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")

public class Expense {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    public Long index;

    @Embedded
    public Transaction transaction;

    //TODO: validator before inserting
    @ColumnInfo(name = "category")
    public String category;

    public Expense(Transaction transaction, String category){
        this.transaction = transaction;
        this.category = category;
        Log.i("Database", "Expense created");
    }

    public String toString(){
        return "Expense: " + String.valueOf(transaction.transactionId);
    }
}