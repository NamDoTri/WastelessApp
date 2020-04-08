package com.wasteless.roomdb.entities;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "wallets")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "walletId")
    public Long walletId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name="balance", defaultValue = "0")
    public double balance;

    @ColumnInfo(name="isBankAccount")
    public boolean isBankAccount;

    public Wallet(String name, double balance, boolean isBankAccount){
        this.name = name;
        this.balance = balance;
        this.isBankAccount = isBankAccount;
        Log.i("Database", "Wallet created: " + String.valueOf(this.walletId));
    }

    public String toString(){
        return "Wallet ID: " + String.valueOf(walletId) + " Balance: " + String.valueOf(balance);
    }
}
