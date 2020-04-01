package com.wasteless.ui.home.goal;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.repository.GoalRepository;
import com.wasteless.roomdb.entities.Goal;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;

    public GoalViewModel(Application application){
        super(application);
        goalRepository = goalRepository.getGoalRepository(application.getApplicationContext());
    }

    public void insertGoalByType(String date, double goal, String type){
        Goal newGoal = new Goal(type, date, goal);
        try{
            goalRepository.insertGoal(newGoal);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteGoalsByType(String type){
        try{
            goalRepository.deleteGoalByType(type);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Goal getGoalByType(String type){
        Goal dailyGoal = null;
        try{
            dailyGoal = goalRepository.getGoalByType(type);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dailyGoal;
    }
}
