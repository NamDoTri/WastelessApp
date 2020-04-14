package com.wasteless.repository;

import android.content.Context;

import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.daos.AchievementDao;
import com.wasteless.roomdb.entities.Achievement;

import java.util.List;

public class AchievementRepository {
    private static volatile AchievementRepository instance = null;
    private final AchievementDao achievementDao;

    private AchievementRepository(Context context){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        achievementDao = db.achievementDao();
    }

    public static AchievementRepository getAchievementRepository(Context context){
        if(instance == null){
            instance = new AchievementRepository(context);
        }
        return instance;
    }
    public  List<Achievement> getAllAchievements() { return achievementDao.getAllAchievements();};

    public void insertGoal(Achievement achievement){
        achievementDao.insert(achievement);
    }

    public void deleteAchievement(Achievement achievement){achievementDao.delete(achievement);}
}
