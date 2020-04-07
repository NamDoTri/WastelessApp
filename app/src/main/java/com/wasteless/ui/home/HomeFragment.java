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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wasteless.R;

import com.wasteless.roomdb.entities.Goal;
import com.wasteless.ui.home.expenses.ExpensesFragment;
import com.wasteless.ui.home.goal.GoalFragment;
import com.wasteless.ui.home.goal.GoalViewModel;
import com.wasteless.ui.home.goal.SliderAdapter;
import com.wasteless.ui.home.goal.SliderModel;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {

    private GoalViewModel goalViewModel;
    private HomeViewModel homeViewModel;
    ViewPager viewPager;
    SliderAdapter adapter;
    List<SliderModel> models;
    String daySpendings;
    private PieChart incomePieChart;
    private boolean usePercentage = true;

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
        final TextView expensesTodayAmount = root.findViewById(R.id.expenses_today_amount);
        final TextView incomeTodayAmount = root.findViewById(R.id.income_today_amount);
        final TextView expensesMonthlyAmount = root.findViewById(R.id.expenses_monthly_amount);
        //final TextView incomeMonthlyAmount = root.findViewById(R.id.income_monthly_amount);

        incomePieChart = ((PieChart)root.findViewById(R.id.income_pie_chart));

        final Button prevWalletButton = root.findViewById(R.id.button_back);
        final Button nextWalletButton = root.findViewById(R.id.button_next);

        expensesMonthlyAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpensesFragment expensesFragment = new ExpensesFragment();

                Bundle expensesBundle = new Bundle();
                expensesBundle.putLong("walletId", homeViewModel.getCurrentWalletId());
                expensesFragment.setArguments(expensesBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, expensesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
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
                expensesTodayAmount.setText(s);
            }
        });
        homeViewModel.getTotalIncomeToday().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                incomeTodayAmount.setText(s);
            }
        });
        homeViewModel.getLiveExpensesByMonth().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                expensesMonthlyAmount.setText(s);
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
            Integer spendings = doubleStringToInteger(homeViewModel.getTotalExpenseTodayBar());
            Integer goalF = doubleStringToInteger(sum.toString());
            Log.i("progress", String.valueOf(spendings));
            Integer progress = (spendings * 100) / goalF;

            createCard("Daily goal", "You have spent "+ progress +"% already",
                    sum.toString(), R.drawable.ic_account_balance_wallet_black_24dp , root, progress);
        } else {
            createCard("Daily goal is not set up yet", "Press the button below",
                    "0",R.drawable.ic_account_balance_wallet_black_24dp , root, 0);
        }
        if(weeklyGoal != null) {
            Double sum = weeklyGoal.amountOfMoney;
            Integer spendings = doubleStringToInteger(homeViewModel.getTotalExpenseWeek());
            Integer goalF = doubleStringToInteger(sum.toString());
            Integer progress = (spendings * 100) / goalF;
            createCard("Weekly goal", "You have spent "+ progress+"%", sum.toString(),
                    R.drawable.month , root, progress);
        } else {
            createCard("Weekly goal is not set up yet", "Press the button below", "0",
                    R.drawable.month , root, 0);
        }
        if(monthlyGoal != null) {
            Double sum = monthlyGoal.amountOfMoney;
            Integer spendings = doubleStringToInteger(homeViewModel.getTotalExpensesByMonth());
            Integer goalF = doubleStringToInteger(sum.toString());
            Integer progress = (spendings * 100) / goalF;
            createCard("Monthly goal", "You have spent "+ progress +"% already",
                    sum.toString(),R.drawable.week_24dp , root, progress);
        } else {
            createCard("Monthly goal is not set up yet", "Press the button below",
                    "0",R.drawable.week_24dp , root, 0);
        }

        root.findViewById(R.id.go_to_goals_button).setOnClickListener(v -> {
            GoalFragment goalFragment = new GoalFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, goalFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        renderMonthlyIncomePieChart();

        // keep track of currently displayed wallet
        homeViewModel.getCurrentlyDisplayWalletIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i("wallet", String.valueOf(homeViewModel.getCurrentlyDisplayWalletIndex().getValue()));
                //TODO
                homeViewModel.updateStats();
                renderMonthlyIncomePieChart();
            }
        });


        return root;
    }

    Integer doubleStringToInteger(String value) {
        String mainChapterNumber = value.split("\\.", 2)[0];
        Integer goalF = Integer.parseInt(mainChapterNumber);
        return goalF;
    };

    private void createCard(String title, String comment, String goal, Integer image, View root, Integer progress) {
        Integer goalF = doubleStringToInteger(goal);

        Log.i("progress", String.valueOf(progress));
        models.add(new SliderModel(image, title, comment, goalF.toString() + " €", progress));
        adapter = new SliderAdapter(models, getContext());
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0 ,100, 0);
    }

    private void renderMonthlyIncomePieChart(){
        PieData incomePieChartData = homeViewModel.getMonthlyIncomePieChart();
        Log.i("chart", incomePieChartData.toString());
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
        //TODO: update this when changing wallet
        incomePieChart.setCenterText(String.valueOf(homeViewModel.getTotalIncomeByMonth())); //TODO: display currency
        incomePieChart.setCenterTextSize(27f);

        // entry label settings
        incomePieChart.setEntryLabelTextSize(17f);
        incomePieChart.setEntryLabelColor(Color.DKGRAY);

        // legends settings
        Legend incomePieChartLegend = incomePieChart.getLegend();
        incomePieChartLegend.setTextSize(15f);

        incomePieChart.notifyDataSetChanged();
        incomePieChart.invalidate();
        incomePieChart.setData(incomePieChartData);

        incomePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            private Entry prevSelectedEntry = null;
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // only if user select the same value again
                if(prevSelectedEntry == null){
                    usePercentage = !usePercentage;
                    incomePieChart.setUsePercentValues(usePercentage);
                }
                prevSelectedEntry = e;
            }

            @Override
            public void onNothingSelected() {
                usePercentage = !usePercentage;
                incomePieChart.setUsePercentValues(usePercentage);
                prevSelectedEntry = null;
            }
        });
    }
}
