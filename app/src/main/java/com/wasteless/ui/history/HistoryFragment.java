package com.wasteless.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.models.TestTransaction;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment{

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    HistoryViewModel historyViewModel;
    //List<TestTransaction> historyList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_history, container, false);
        View root = inflater.inflate(R.layout.fragment_historylist, container, false);

        recyclerView = root.findViewById(R.id.history_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getHistoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<TestTransaction>>() {
            @Override
            public void onChanged(@Nullable List<TestTransaction> testTransactions) {
                recyclerAdapter.setTestTransactions(testTransactions);
            }
        });

        /*root.findViewById(R.id.history_transaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionFragment transactionFragment = new TransactionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, transactionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/

        return root;
    }
}
