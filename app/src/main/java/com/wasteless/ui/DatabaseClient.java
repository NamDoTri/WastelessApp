package com.wasteless.ui;


import android.content.Context;

import androidx.room.Room;

import com.wasteless.roomdb.AppDatabase;

public class DatabaseClient {
    private static AppDatabase db = null;

    private DatabaseClient(Context context){
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "wasteless-db").build();
    }
    public static AppDatabase getDatabaseClient(Context context){
        if(db == null){
            new DatabaseClient(context);
        }
        return db;
    }
}
