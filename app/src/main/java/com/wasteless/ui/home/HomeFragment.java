package com.wasteless.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wasteless.R;

import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.home.goal.GoalFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView budgetAmount = root.findViewById(R.id.budget_amount);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);
        final TextView expensesAmount = root.findViewById(R.id.expenses_amount);
        final TextView incomeAmount = root.findViewById(R.id.income_amount);
        final PieChart expensePieChart = root.findViewById(R.id.expenses_pie_chart);

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

        homeViewModel.getIncomesAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                incomeAmount.setText(s);
            }
        });

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

        homeViewModel.getMonthlyExpenses().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                ArrayList<PieEntry> entries = new ArrayList<>();
                /*Map<String, List<Transaction>> map = new HashMap<String, List<Transaction>>();
                for (Transaction transaction : transactions){
                    String key = transaction.type;
                    if (map.get(key) == null){
                        map.put(key, new ArrayList<Transaction>());
                    }
                    map.get(key).add(transaction);
                }*/


                for (int i=0; i<transactions.size(); i++){
                    entries.add(new PieEntry((float) transactions.get(i).amount, transactions.get(i).description));
                }

                    //entries.add(new PieEntry(FLOAT(amount), STRING(type) ));
                /*float values[] = {123.4f, 98.7f};

                String descriptions[] = {"test1", "test2"};

                for (int i=0; i<values.length; i++){
                    entries.add(new PieEntry(values[i], descriptions[i]));*/

                    PieDataSet dataSet = new PieDataSet(entries, "Expenses");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(15f);
                    data.setValueTextColor(Color.WHITE);

                    expensePieChart.setData(data);
                    expensePieChart.invalidate();


                    Context context = getContext();
                    CharSequence text = String.valueOf(entries.size());
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
        });

        return root;
    }

}
