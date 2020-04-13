package com.wasteless.ui.settings.achievements;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Achievement;
import com.wasteless.ui.settings.SettingsViewModel;
import com.wasteless.ui.settings.achievements.AchievementViewModel;

import java.util.List;

public class AchievementFragment extends Fragment {
    private SettingsViewModel SettingsViewModel;
    private AchievementViewModel achievementViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        achievementViewModel = ViewModelProviders.of(this).get(AchievementViewModel.class);
        View NotificationsFragment = inflater.inflate(R.layout.achievements_fragment, container, false);


        return NotificationsFragment;
    }
}
