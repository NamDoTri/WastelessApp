package com.wasteless.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
//import com.wasteless.models.TestTransaction;
import com.wasteless.models.TestTransaction;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.home.HomeViewModel;
import com.wasteless.ui.transaction.TransactionAdapter;
import com.wasteless.ui.transaction.TransactionFragment;

import java.util.List;

public class HistoryFragment extends Fragment{

    private TransactionAdapter transactionAdapter;
    private TempAdapter tempAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);

        RecyclerView recyclerView = root.findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*transactionAdapter = new TransactionAdapter();
        recyclerView.setAdapter(transactionAdapter);

        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getHistoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<TestTransaction>>() {
            @Override
            public void onChanged(@Nullable List<TestTransaction> testTransactions) {
                transactionAdapter.setTestTransactions(testTransactions);
            }
        });*/

        tempAdapter = new TempAdapter();
        recyclerView.setAdapter(tempAdapter);

        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                tempAdapter.setTransactions(transactions);
            }
        });

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getBalanceAmount().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                balanceAmount.setText(s);
            }
        });

        tempAdapter.setOnTransactionClickListener(new TempAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                TransactionFragment transactionFragment = new TransactionFragment();

                //I think that in the end I only need to transfer the ID of the item that was clicked
                //since we should pull more data from the db in the transaction details-fragment (like actual date, wallet etc).

                //So this is just to test out the process of changing fragments and to design the details
                Bundle transactionBundle = new Bundle();
                transactionBundle.putString("description", transaction.getDescription());
                //transactionBundle.putString("category", transaction.getCategory());
                transactionBundle.putString("amount", String.valueOf(transaction.getAmount()));
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, transactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
