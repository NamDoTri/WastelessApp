package com.wasteless.ui.home.goal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

import com.wasteless.ui.home.HomeViewModel;

public class GoalFragment extends Fragment {
    private HomeViewModel homeViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View goalFragmentView = inflater.inflate(R.layout.fragment_goal, container, false);
        goalFragmentView.findViewById(R.id.submit_goals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dailyGoal = ((EditText) goalFragmentView.findViewById(R.id.daily_goal)).getText().toString();
                String weeklyGoal = ((EditText) goalFragmentView.findViewById(R.id.weekly_goal)).getText().toString();
                Log.i("goals", dailyGoal);
            }
        });

        return goalFragmentView;
    }
}
