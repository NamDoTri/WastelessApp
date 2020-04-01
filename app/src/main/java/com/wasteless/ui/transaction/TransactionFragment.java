package com.wasteless.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.wasteless.R;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.edit_transaction.EditTransactionFragment;

public class TransactionFragment extends Fragment {

    private TransactionViewModel transactionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);

        final Bundle transactionBundle = this.getArguments();

        final Transaction transaction = transactionViewModel.getTransactionById(transactionBundle.getLong("id"));

        final TextView header = root.findViewById(R.id.transaction_text);
        final TextView amount = root.findViewById(R.id.amount);
        final TextView type = root.findViewById(R.id.type);
        final TextView description = root.findViewById(R.id.description);
        final TextView date = root.findViewById(R.id.date);
        final TextView wallet = root.findViewById(R.id.wallet);
        Log.d("transaction", "date: " + transaction.date);
        //final TextView source = root.findViewById(R.id.source);

        //TO-DO ---> check if the transaction is either expense or income and modify data shown related to that
        //for example show the source and make the amount red/green

        //TO-DO pt.2 ---> check that how many tags the transaction has and show only the correct amount

        Context context = getContext();
        CharSequence text = "TransactionID is " + transaction.transactionId;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                header.setText(s);
                amount.setText(String.valueOf(transaction.amount));
                description.setText(transaction.description);
                type.setText(transaction.type);
                date.setText(transaction.date);
                wallet.setText(String.valueOf(transaction.wallet));
            }
        });

        root.findViewById(R.id.transaction_edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTransactionFragment editTransactionFragment = new EditTransactionFragment();
                editTransactionFragment.setArguments(transactionBundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, editTransactionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Edit fragment with a form goes here ---> sending a bundle filled with transaction
                //data to pre-fill the form?


                Context context = getContext();
                CharSequence text = "Edit clicked";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        root.findViewById(R.id.transaction_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CONFIRMATION SCREEN HEREEEEEEEEEEEEEEE SO THERE WONT BE ACCIDENTAL DELETIONS

                //Delete transaction
                transactionViewModel.delete(transaction);

                Context context = getContext();
                CharSequence text = "Transaction deleted";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return root;
    }

}
