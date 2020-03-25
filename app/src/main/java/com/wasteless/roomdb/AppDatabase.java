package com.wasteless.roomdb;

import com.wasteless.roomdb.entities.*;
import com.wasteless.roomdb.daos.*;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BankAccount.class, Wallet.class, Tag.class, TagAssociation.class, Transaction.class},
          version=1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BankAccountDao bankAccountDao();
    public abstract WalletDao walletDao();
    public abstract TransactionDao transactionDao();
}
