package com.wasteless.ui.home.goal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
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
    ArrayList<String> arrayToHideButton = new ArrayList<String>();
    Integer amountOfGoals = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View goalFragmentView = inflater.inflate(R.layout.fragment_goal, container, false);
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);

        Button submit_goals_button = goalFragmentView.findViewById(R.id.submit_goals);

        final LinearLayout day_part = goalFragmentView.findViewById(R.id.day_part);
        final TextView day_goal_text = goalFragmentView.findViewById(R.id.day_goal_text);
        final  EditText day_goal_edit = goalFragmentView.findViewById(R.id.day_goal_value);

        final LinearLayout month_part = goalFragmentView.findViewById(R.id.month_part);
        final TextView month_goal_text = goalFragmentView.findViewById(R.id.month_goal_text);
        final  EditText month_goal_edit = goalFragmentView.findViewById(R.id.month_goal_value);

        Goal dailyGoal = goalViewModel.getGoalByType("daily");
        Goal monthlyGoal = goalViewModel.getGoalByType("monthly");

        if(dailyGoal != null) {
            day_part.setVisibility(View.GONE);
            day_goal_text.setVisibility(View.VISIBLE);
            arrayToHideButton.add("test");
            day_goal_text.setText("Your goal for today is "+ dailyGoal.amountOfMoney);
        }
        if(monthlyGoal != null) {
            month_part.setVisibility(View.GONE);
            month_goal_text.setVisibility(View.VISIBLE);
            arrayToHideButton.add("test");
            month_goal_text.setText("Your goal for the month is "+ monthlyGoal.amountOfMoney);
        }
        if(arrayToHideButton.size() == amountOfGoals){
            submit_goals_button.setVisibility(View.GONE);
        }
        day_goal_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_goals_button.setVisibility(View.VISIBLE);
                day_part.setVisibility(View.VISIBLE);
                day_goal_text.setVisibility(View.GONE);
                arrayToDeletePreviousGoals.add("daily");
            }
        });

        month_goal_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_goals_button.setVisibility(View.VISIBLE);
                month_part.setVisibility(View.VISIBLE);
                month_goal_text.setVisibility(View.GONE);
                arrayToDeletePreviousGoals.add("monthly");
            }
        });
        submit_goals_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = takeCurrentDate(goalFragmentView);

                for(int i = 0; i < arrayToDeletePreviousGoals.size(); i++){
                    goalViewModel.deleteGoalsByType(arrayToDeletePreviousGoals.get(i));
                }
                if(day_goal_edit.getText().toString().length() != 0){
                    Log.i("goal", "Im erer");
                    goalViewModel.insertGoalByType(date, Double.parseDouble(day_goal_edit.getText().toString()), "daily");
                }
                if(month_goal_edit.getText().toString().length() != 0){
                    goalViewModel.insertGoalByType(date, Double.parseDouble(month_goal_edit.getText().toString()), "monthly");
                }

                Log.i("goals", day_goal_edit.getText().toString());
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
