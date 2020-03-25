package com.wasteless.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.models.TestTransaction;
import com.wasteless.ui.transaction.TransactionFragment;

import java.util.List;

public class HistoryFragment extends Fragment{

    private TransactionAdapter transactionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionAdapter = new TransactionAdapter();
        recyclerView.setAdapter(transactionAdapter);

        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getHistoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<TestTransaction>>() {
            @Override
            public void onChanged(@Nullable List<TestTransaction> testTransactions) {
                transactionAdapter.setTestTransactions(testTransactions);
            }
        });

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(TestTransaction testTransaction) {
                TransactionFragment transactionFragment = new TransactionFragment();

                //I think that in the end I only need to transfer the ID of the item that was clicked
                //since we should pull more data from the db in the transaction details-fragment (like actual date, wallet etc).

                //So this is just to test out the process of changing fragments and to design the details
                Bundle transactionBundle = new Bundle();
                transactionBundle.putString("description", testTransaction.getDescription());
                transactionBundle.putString("category", testTransaction.getCategory());
                transactionBundle.putString("amount", testTransaction.getAmount());
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, transactionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
