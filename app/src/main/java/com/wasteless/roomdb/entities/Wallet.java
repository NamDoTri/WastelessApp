package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    public int walletId;

    @ColumnInfo(name="balance", defaultValue = "0")
    public double balance;
}
