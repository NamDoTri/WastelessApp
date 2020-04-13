package com.wasteless.ui.settings.achievements;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.repository.AchievementRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.Achievement;
import com.wasteless.ui.home.goal.GoalViewModel;

import java.util.List;

public class AchievementViewModel extends AndroidViewModel {
    private AchievementRepository achievementRepository;

    public AchievementViewModel(@NonNull Application application) {
        super(application);
        achievementRepository = AchievementRepository.getAchievementRepository(application.getApplicationContext());
    }

    public List<Achievement> getAllAchievements(){
        return achievementRepository.getAllAchievements();
    }
}
