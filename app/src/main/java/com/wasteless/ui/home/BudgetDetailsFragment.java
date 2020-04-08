package com.wasteless.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wasteless.R;

public class BudgetDetailsFragment extends Fragment {
    private HomeViewModel homeViewModel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_budget, container, false);

        return root;
    }
}
