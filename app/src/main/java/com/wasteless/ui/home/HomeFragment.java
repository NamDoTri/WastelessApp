package com.wasteless.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

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

        homeViewModel.getBudgetAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                budgetAmount.setText(s);
            }
        });

        homeViewModel.getBalanceAmount().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                balanceAmount.setText(s);
            }
        });

        homeViewModel.getExpensesAmount().observe(getViewLifecycleOwner(), new Observer<String>(){
           @Override
           public void onChanged(@Nullable String s){
               expensesAmount.setText(s);
           }
        });

        homeViewModel.getIncomesAmount().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(String s){
                incomeAmount.setText(s);
            }
        });
        return root;
    }
}
