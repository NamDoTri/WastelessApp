package com.wasteless.roomdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "isMonthBudget")
    public boolean isMonthBudget;

    @ColumnInfo(name = "createdAt")
    public String createdAt;

    public Budget(double amount, String createdAt, boolean isMonthBudget){
        this.amount = amount;
        this.createdAt = createdAt;
        this.isMonthBudget = isMonthBudget;
    }
}
