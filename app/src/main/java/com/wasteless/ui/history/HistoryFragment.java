package com.wasteless.ui.history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Tag;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.home.HomeViewModel;
import com.wasteless.ui.search.SearchViewModel;
import com.wasteless.ui.transaction.TransactionAdapter;
import com.wasteless.ui.transaction.TransactionFragment;

import java.util.List;

public class HistoryFragment extends Fragment{

    private SearchViewModel searchViewModel;
    private TransactionAdapter transactionAdapter;
    private List<Tag> tags;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView balanceAmount = root.findViewById(R.id.balance_amount);

        RecyclerView recyclerView = root.findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tags = searchViewModel.getTags();
        transactionAdapter = new TransactionAdapter();
        recyclerView.setAdapter(transactionAdapter);

        final HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                transactionAdapter.setTransactions(transactions, tags);
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

                Bundle transactionBundle = new Bundle();
                transactionBundle.putLong("id", transaction.transactionId);
                transactionFragment.setArguments(transactionBundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, transactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //TODO: confirmation screen & make the swipe animation red
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(getActivity());
                deleteAlert.setTitle("Delete");
                deleteAlert.setMessage("Are you sure you want to delete this transaction?");
                deleteAlert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        transactionAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                deleteAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete transaction
                        historyViewModel.delete(transactionAdapter.getTransactionAt(viewHolder.getAdapterPosition()));

                        Context context = getContext();
                        CharSequence text = "Transaction deleted";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
                deleteAlert.show();
            }
        }).attachToRecyclerView(recyclerView);

        return root;
    }
}
