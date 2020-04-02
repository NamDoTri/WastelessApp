package com.wasteless.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.wasteless.R;

import com.wasteless.roomdb.entities.Goal;
import com.wasteless.ui.home.goal.GoalFragment;
import com.wasteless.ui.home.goal.GoalViewModel;
import com.wasteless.ui.home.goal.SliderAdapter;
import com.wasteless.ui.home.goal.SliderModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private GoalViewModel goalViewModel;
    private HomeViewModel homeViewModel;
    ViewPager viewPager;
    SliderAdapter adapter;
    List<SliderModel> models;
    private PieChart incomePieChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        View cardView = inflater.inflate(R.layout.item, container, false);
        models = new ArrayList<>();
        final TextView walletTitle = root.findViewById(R.id.wallet_title);
        final TextView budgetAmount = root.findViewById(R.id.budget_amount);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);
        final TextView expensesAmount = root.findViewById(R.id.expenses_amount);
        final TextView incomeAmount = root.findViewById(R.id.income_amount);

        incomePieChart = ((PieChart)root.findViewById(R.id.income_pie_chart));
        final PieChart expensePieChart = root.findViewById(R.id.expenses_pie_chart);
        final BarChart expenseBarChart = root.findViewById(R.id.expenses_bar_chart);

        final Button prevWalletButton = root.findViewById(R.id.button_back);
        final Button nextWalletButton = root.findViewById(R.id.button_next);


        prevWalletButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeViewModel.changeWallet("prev");
            }
        });
        nextWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.changeWallet("next");
            }
        });
        homeViewModel.getCurrentWalletName().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s) { walletTitle.setText(s); }
        });
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

        homeViewModel.getTotalExpenseToday().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        //
        // Goals and their slider
        //

        Goal dailyGoal = goalViewModel.getGoalByType("daily");
        Goal weeklyGoal = goalViewModel.getGoalByType("weekly");
        Goal monthlyGoal = goalViewModel.getGoalByType("monthly");

        if(dailyGoal != null) {
            Double sum = dailyGoal.amountOfMoney;
            createCard("Daily goal", "You have saved nothing ",
                    sum.toString(), R.drawable.ic_account_balance_wallet_black_24dp , root);
        } else {
            createCard("Daily goal is not set up yet", "Press the button below",
                    "0",R.drawable.ic_account_balance_wallet_black_24dp , root);
        }
        if(weeklyGoal != null) {
            Log.i("goal", "asdasdad");
            Double sum = weeklyGoal.amountOfMoney;
            createCard("Weekly goal", "You have saved nothing ", sum.toString(),
                    R.drawable.month , root);
        } else {
            createCard("Weekly goal is not set up yet", "Press the button below", "0",
                    R.drawable.month , root);
        }
        if(monthlyGoal != null) {
            Double sum = monthlyGoal.amountOfMoney;
            createCard("Monthly goal", "You have saved nothing ",
                    sum.toString(),R.drawable.week_24dp , root);
        } else {
            createCard("Monthly goal is not set up yet", "Press the button below",
                    "0",R.drawable.week_24dp , root);
        }

        root.findViewById(R.id.go_to_goals_button).setOnClickListener(v -> {
            GoalFragment goalFragment = new GoalFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, goalFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        renderMonthlyIncomePieChart();

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
        //TODO: get labels(dates) from viewmodel or just set them here somehow
        BarData expenseBarChartData = homeViewModel.getExpenseBarChart();

        //Axis settings
        expenseBarChart.getAxisLeft().setAxisMinimum(0);
        expenseBarChart.getAxisRight().setAxisMinimum(0);

        //Grid settings
        expenseBarChart.getAxisLeft().setDrawGridLines(false);
        expenseBarChart.getXAxis().setDrawGridLines(false);

        //Description settings
        expenseBarChart.getXAxis().setDrawLabels(false);
        expenseBarChart.getLegend().setEnabled(false);
        expenseBarChart.getDescription().setEnabled(false);

        expenseBarChart.setData(expenseBarChartData);


        // keep track of currently displayed wallet
        homeViewModel.getCurrentlyDisplayWalletIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i("wallet", String.valueOf(homeViewModel.getCurrentlyDisplayWalletIndex().getValue()));
                //TODO
                homeViewModel.updateStats();
            }
        });


        return root;
    }

    private void createCard(String title, String comment, String goal, Integer image, View root) {
        String mainChapterNumber = goal.split("\\.", 2)[0];
        Integer goalF = Integer.parseInt(mainChapterNumber);
        models.add(new SliderModel(image, title, comment, goalF.toString() + " €") );
        adapter = new SliderAdapter(models, getContext());
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0 ,100, 0);
    }

    private void renderMonthlyIncomePieChart(){
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
    }
}
