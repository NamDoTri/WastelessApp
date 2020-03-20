package com.wasteless.roomdb.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "bankaccounts",
        foreignKeys = @ForeignKey(entity = Wallet.class ,
        parentColumns = "wallets.walletId",
        childColumns = "bankaccounts.walletId",
        onDelete = ForeignKey.CASCADE))

public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    public Long walletId;

    @ColumnInfo(name = "ibanNumber")
    @NonNull
    public String ibanNumber;

    @ColumnInfo(name = "token")
    // @NonNull
    public String token;
}
