package com.wasteless.roomdb.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    public int walletId;

    @ColumnInfo(name="balance", defaultValue = "0")
    public double balance;

    @ColumnInfo(name = "ibanNumber")
    @NonNull
    public String ibanNumber;

    @ColumnInfo(name = "token")
    // @NonNull
    public String token;
}
