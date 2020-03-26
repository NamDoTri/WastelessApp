package com.wasteless.roomdb;

import android.content.Context;

import com.wasteless.roomdb.entities.*;
import com.wasteless.roomdb.daos.*;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {BankAccount.class, Wallet.class, Tag.class, TagAssociation.class, Transaction.class},
          version=1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance = null; //write to main memory, not to cache
    private static Context context;

    public abstract BankAccountDao bankAccountDao();
    public abstract WalletDao walletDao();
    public abstract TransactionDao transactionDao();

    public static synchronized AppDatabase getAppDatabase(Context context){
        context = context;

        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "wastelessDB")
                    .allowMainThreadQueries()
                    .addCallback(prepopulateData)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback prepopulateData = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    getAppDatabase(context).walletDao().insertAll(
                            new Wallet("default", 400)
                    );
                    getAppDatabase(context).transactionDao().insertAll(
                            new Transaction("22/02/2020", 3445.0, "wage", Long.valueOf(1), true, "salary", "street"),
                            new Transaction("21/02/2020", 34.0, "cheese", Long.valueOf(1), false, "Food")
                    );
                }
            });
        }
    };
}
