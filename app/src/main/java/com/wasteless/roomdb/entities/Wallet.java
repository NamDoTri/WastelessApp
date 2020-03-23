package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "wallets")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "walletId")
    public Long walletId;

    @ColumnInfo(name="balance", defaultValue = "0")
    public double balance;

    public Wallet(double balance){
        this.balance = balance;
    }

    public String toString(){
        return "Wallet ID: " + String.valueOf(walletId) + " Balance: " + String.valueOf(balance);
    }
}
