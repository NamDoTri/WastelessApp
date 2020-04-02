package com.wasteless.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.wasteless.R;

import com.wasteless.roomdb.entities.Goal;
import com.wasteless.ui.home.goal.GoalFragment;
import com.wasteless.ui.home.goal.GoalViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private GoalViewModel goalViewModel;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView budgetAmount = root.findViewById(R.id.budget_amount);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);
        final TextView expensesAmount = root.findViewById(R.id.expenses_amount);
        final TextView incomeAmount = root.findViewById(R.id.income_amount);

        final PieChart incomePieChart = ((PieChart)root.findViewById(R.id.income_pie_chart));
        final PieChart expensePieChart = root.findViewById(R.id.expenses_pie_chart);
        final BarChart expenseBarChart = root.findViewById(R.id.expenses_bar_chart);

        homeViewModel.getBudgetAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                budgetAmount.setText(s);
            }
        });

        homeViewModel.getBalanceAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                balanceAmount.setText(s);
            }
        });

        homeViewModel.getExpensesAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                expensesAmount.setText(s);
            }
        });

        homeViewModel.getTotalIncomeToday().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                incomeAmount.setText(s);
            }
        });

        Goal dailyGoal = goalViewModel.getGoalByType("daily");
        TextView goals = root.findViewById(R.id.goal_value);
        if(dailyGoal != null) {
            Log.i("goal", "asdasdad");
            goals.setText("Your daily goal \n"+dailyGoal.amountOfMoney);
        }

        root.findViewById(R.id.add_goal_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoalFragment goalFragment = new GoalFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, goalFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        PieData incomePieChartData = homeViewModel.getMonthlyIncomePieChart();
        // value settings
        incomePieChartData.setValueTextSize(20f);
        incomePieChartData.setValueTextColor(Color.DKGRAY);
        incomePieChartData.setValueFormatter(new PercentFormatter(incomePieChart)); //TODO: render value inside the slices

        //// chart settings
        incomePieChart.setUsePercentValues(true);
        incomePieChart.setTransparentCircleRadius(35f);
        incomePieChart.setHoleRadius(30f);
        incomePieChart.getDescription().setEnabled(false);

        // center text settings
        incomePieChart.setCenterText(String.valueOf(homeViewModel.getTotalIncomeByMonth())); //TODO: display currency
        incomePieChart.setCenterTextSize(27f);

        // entry label settings
        incomePieChart.setEntryLabelTextSize(17f);
        incomePieChart.setEntryLabelColor(Color.DKGRAY);

        // legends settings
        Legend incomePieChartLegend = incomePieChart.getLegend();
        incomePieChartLegend.setTextSize(15f);

        incomePieChart.setData(incomePieChartData);


        //EXPENSE PIE CHART
        PieData expensePieChartData = homeViewModel.getMonthlyExpensePieChart();

        //Value settings
        expensePieChartData.setValueTextSize(20f);
        expensePieChartData.setValueTextColor(Color.DKGRAY);
        expensePieChartData.setValueFormatter(new PercentFormatter(incomePieChart));

        //Chart settings
        expensePieChart.setUsePercentValues(true);
        expensePieChart.setTransparentCircleRadius(35f);
        expensePieChart.setHoleRadius(30f);
        expensePieChart.getDescription().setEnabled(false);

        //Center text settings
        expensePieChart.setCenterText(String.valueOf(homeViewModel.getTotalExpensesByMonth()));
        expensePieChart.setCenterTextSize(27f);

        //Entry label settings --- Removing the labels for now since sometimes they seem to overlap
        expensePieChart.setDrawEntryLabels(false);

        //Legend settings
        Legend expensePieChartLegend = expensePieChart.getLegend();
        expensePieChartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        expensePieChartLegend.setTextSize(15f);

        expensePieChart.setData(expensePieChartData);

        //EXPENSE BAR CHART --- idea: stack expenses per category in different colors for each day
        BarData expenseBarChartData = homeViewModel.getExpenseBarChart();
        XAxis xAxis = expenseBarChart.getXAxis();
        YAxis yAxisLeft = expenseBarChart.getAxisLeft();
        YAxis yAxisRight =  expenseBarChart.getAxisRight();

        //Label settings
        ArrayList dates = homeViewModel.getDateLabels();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size()/2);

        //Axis settings
        yAxisLeft.setAxisMinimum(0);
        yAxisRight.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Grid settings
        yAxisLeft.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        
        //Descriptive settings
        expenseBarChart.getLegend().setEnabled(false);
        expenseBarChart.getDescription().setEnabled(false);

        expenseBarChart.setData(expenseBarChartData);

        return root;
    }
}
