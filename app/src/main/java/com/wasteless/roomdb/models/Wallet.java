package com.wasteless.roomdb.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    private int walletId;

    @ColumnInfo(name="balance", defaultValue = "0")
    private double balance;
}
