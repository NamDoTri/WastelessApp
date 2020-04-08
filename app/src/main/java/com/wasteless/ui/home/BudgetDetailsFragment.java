package com.wasteless.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wasteless.R;

public class BudgetDetailsFragment extends Fragment {
    private HomeViewModel homeViewModel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_budget, container, false);

        root.findViewById(R.id.set_budget_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String amount = ((EditText)root.findViewById(R.id.input_budget)).getText().toString();
                homeViewModel.setBudget(amount); //TODO: "nothing changed" message
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, homeFragment);
                transaction.commit();
            }
        });

        return root;
    }
}
