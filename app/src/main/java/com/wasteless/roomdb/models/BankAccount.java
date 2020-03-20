package com.wasteless.roomdb.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    private int walletId;

    @ColumnInfo(name="balance", defaultValue = "0")
    private double balance;

    @ColumnInfo(name = "ibanNumber")
    @NonNull
    private String ibanNumber;

    @ColumnInfo(name = "token")
    // @NonNull
    private String token;
}
