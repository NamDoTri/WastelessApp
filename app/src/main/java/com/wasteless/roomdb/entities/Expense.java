package com.wasteless.roomdb.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses",
        foreignKeys = @ForeignKey(entity = Wallet.class,
        parentColumns = "walletId",
        childColumns = "wallet",
        onDelete = ForeignKey.CASCADE))

public class Expense {
    @PrimaryKey(autoGenerate = true)
    public Long expenseId;

    //TODO: use @TypeConverter
    @ColumnInfo(name = "date")
    @NonNull
    public String date;

    @ColumnInfo(name = "amount")
    @NonNull
    public double amount;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "wallet")
    public Long wallet;

    //array of tags

    //enum of Category
    //@NonNull
}