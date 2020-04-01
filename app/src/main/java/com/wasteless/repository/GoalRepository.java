package com.wasteless.repository;

import android.content.Context;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.GoalDao;
import com.wasteless.roomdb.daos.WalletDao;
import com.wasteless.roomdb.entities.Goal;
import com.wasteless.roomdb.entities.Wallet;

import java.util.List;

public class GoalRepository {
    private static volatile GoalRepository instance = null;
    private final GoalDao goalDao;

    private GoalRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        goalDao = db.goalDao();
    }

    public static GoalRepository getGoalRepository(Context context){
        if(instance == null){
            instance = new GoalRepository(context);
        }
        return instance;
    }


    public Goal getGoalByType(String type){return goalDao.getGoaByType(type);}


    public void insertGoal(Goal goal){
        goalDao.insertAll(goal);
    }

    public void deleteGoalByType(String type){goalDao.deleteGoalByType(type);}


}
