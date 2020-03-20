package com.wasteless.roomdb.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;

@Entity(tableName = "incomes",
        foreignKeys = @ForeignKey(entity = Wallet.class,
                parentColumns = "walletId",
                childColumns = "wallet",
                onDelete = ForeignKey.CASCADE))

public class Income {
    @PrimaryKey(autoGenerate = true)
    public Long incomeId;

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

    //TODO: array of tags

    @ColumnInfo(name = "source")
    public String source;

    //TODO: validator before inserting/updating
    @ColumnInfo(name = "type")
    public String type;
}
