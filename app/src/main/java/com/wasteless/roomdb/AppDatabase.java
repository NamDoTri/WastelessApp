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

@Database(entities = {BankAccount.class, Wallet.class, Tag.class, TagAssociation.class, Transaction.class, Goal.class},
          version=1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance = null; //write to main memory, not to cache
    private static Context context; //TODO: remove this in production

    public abstract TagDao tagDao();
    public abstract WalletDao walletDao();
    public abstract TransactionDao transactionDao();
    public abstract GoalDao goalDao();

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
                            new Wallet("wallet 1", 400, false),
                            new Wallet("wallet 2",500, false)
                    );
                    getAppDatabase(context).goalDao().insertAll(
                            new Goal("month","31.3.2020", 123.4)
                    );
                    getAppDatabase(context).transactionDao().insertAll(
                            new Transaction("02/04/2020", 3445.0, "wage", Long.valueOf(1), true, "salary", "street"),
                            new Transaction("07/04/2020", 45.0, "stolen", Long.valueOf(1), true, "salary", "friend"),
                            new Transaction("07/04/2020", 10.0, "netflix", Long.valueOf(1), false, "Entertainment"),
                            new Transaction("13/04/2020", 34.0, "cheese", Long.valueOf(1), false, "Groceries"),
                            new Transaction("21/04/2020", 56.0, "beer", Long.valueOf(1), false, "Groceries"),
                            new Transaction("26/04/2020", 34.0, "cheesecake", Long.valueOf(1), false, "Groceries")
                    );
                    getAppDatabase(context).tagDao().insertAll(
                            new Tag("default tag")
                    );
                }
            });
        }
    };
}
