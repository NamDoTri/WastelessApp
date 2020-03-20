package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(entity = Wallet.class,
                                parentColumns = "walletId",
                                childColumns = "wallet",
                                onDelete = ForeignKey.CASCADE))
public class Transaction {
    @ColumnInfo(name = "transactionId")
    @PrimaryKey(autoGenerate = true)
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

    //tags are stored in a separate table
}
