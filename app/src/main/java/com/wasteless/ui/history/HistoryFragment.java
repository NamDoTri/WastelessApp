package com.wasteless.ui.history;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
//import com.wasteless.models.TestTransaction;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.home.HomeViewModel;
import com.wasteless.ui.transaction.TransactionAdapter;
import com.wasteless.ui.transaction.TransactionFragment;

import java.util.List;

public class HistoryFragment extends Fragment{

    private TransactionAdapter transactionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);

        RecyclerView recyclerView = root.findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionAdapter = new TransactionAdapter();
        recyclerView.setAdapter(transactionAdapter);

        final HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                transactionAdapter.setTransactions(transactions);
            }
        });

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getBalanceAmount().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(@Nullable String s){
                balanceAmount.setText(s);
            }
        });

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                TransactionFragment transactionFragment = new TransactionFragment();

                //I think that in the end I only need to transfer the ID of the item that was clicked
                //since we should pull more data from the db in the transaction details-fragment (like actual date, wallet etc).

                //So this is just to test out the process of changing fragments and to design the details
                Bundle transactionBundle = new Bundle();
                transactionBundle.putString("id", String.valueOf(transaction.transactionId));
                transactionBundle.putString("description", transaction.description);
                transactionBundle.putString("amount", String.valueOf(transaction.amount));
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, transactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //CREATE A CONFIRMATION FOR THIS!1!1!!!!1!1
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                historyViewModel.delete(transactionAdapter.getTransactionAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Transaction deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

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
