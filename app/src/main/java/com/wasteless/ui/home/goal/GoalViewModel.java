package com.wasteless.ui.home.goal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.repository.GoalRepository;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Goal;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;

    public GoalViewModel(Application application){
        super(application);
        goalRepository = goalRepository.getGoalRepository(application.getApplicationContext());
    }
    public void insertGoal(String type, String date, double goal){
        Goal newGoal = new Goal(type, date, goal);
        try{
            goalRepository.insertGoal(newGoal);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
