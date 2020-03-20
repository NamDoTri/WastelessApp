package com.wasteless.roomdb;

import com.wasteless.roomdb.entities.*;
import com.wasteless.roomdb.daos.*;

import androidx.room.Database;

@Database(entities = {BankAccount.class, Expense.class, Income.class, Wallet.class}, version=1)
public abstract class DatabaseConstruct {
    public abstract BankAccountDao bankAccountDao();
    public abstract ExpenseDao expenseDao();
    public abstract IncomeDao incomeDao();
    public abstract Wallet walletDao();
}
