package com.wasteless.ui.home.goal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;

import com.wasteless.ui.home.HomeViewModel;

public class GoalFragment extends Fragment {
    private HomeViewModel homeViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View goalFragmentView = inflater.inflate(R.layout.fragment_goal, container, false);

        return goalFragmentView;
    }
}
