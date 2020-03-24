package com.wasteless.ui;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.*;

import java.util.concurrent.Executors;

public class DatabaseClient {
    private static AppDatabase db = null;
    private static Context context;

    private DatabaseClient(Context context){
        this.context = context;
        db = Room.databaseBuilder(this.context.getApplicationContext(), AppDatabase.class, "wasteless-db")
                .allowMainThreadQueries() //TODO: remove this after testing
                .addCallback(prepopulateData).build();
    }
    public static AppDatabase getDatabaseClient(Context context){
        if(db == null){
            new DatabaseClient(context);
        }
        return db;
    }
    private RoomDatabase.Callback prepopulateData = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //this line doesnt run for some reason
                    getDatabaseClient(context).expenseDao().insertAll(
                            new Expense(new Transaction("21/02/2020", 20.2, "cheese", Long.valueOf(1)), "Food")
                    );
                    getDatabaseClient(context).walletDao().insertAll(
                            new Wallet(400)
                    );
                }
            });
        }
    };
}
