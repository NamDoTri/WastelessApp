package com.wasteless.ui.home.goal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

import com.wasteless.roomdb.entities.Goal;
import com.wasteless.ui.home.HomeViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GoalFragment extends Fragment {
    private GoalViewModel goalViewModel;
    ArrayList<String> arrayToDeletePreviousGoals = new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View goalFragmentView = inflater.inflate(R.layout.fragment_goal, container, false);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        final LinearLayout day_part = goalFragmentView.findViewById(R.id.day_part);
        final TextView day_goal_text = goalFragmentView.findViewById(R.id.day_goal_text);

        Goal dailyGoal = goalViewModel.getDailyGoal();

        if(goalViewModel.getDailyGoal() != null) {
            day_part.setVisibility(View.GONE);
            day_goal_text.setVisibility(View.VISIBLE);
            day_goal_text.setText("Your goal for today is "+ dailyGoal.amountOfMoney);
        }
        day_goal_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_part.setVisibility(View.VISIBLE);
                day_goal_text.setVisibility(View.GONE);
                arrayToDeletePreviousGoals.add("daily");
            }
        });
        goalFragmentView.findViewById(R.id.submit_goals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = takeCurrentDate(goalFragmentView);
                String dailyGoal = ((EditText) goalFragmentView.findViewById(R.id.daily_goal)).getText().toString();
                String monthlyGoal = ((EditText) goalFragmentView.findViewById(R.id.monthly_goal)).getText().toString();

                for(int i = 0; i < arrayToDeletePreviousGoals.size(); i++){
                    goalViewModel.deleteGoalsByType(arrayToDeletePreviousGoals.get(i));
                }
                goalViewModel.insertDailyGoal(date, Double.parseDouble(dailyGoal));
                Log.i("goals", dailyGoal);
                GoalFragment goalFragment = new GoalFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, goalFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return goalFragmentView;
    }

    private String takeCurrentDate(View goalFragmentView) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }
}
