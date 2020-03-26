package com.wasteless.ui.transaction;

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

public class TransactionFragment extends Fragment {

    private TransactionViewModel transactionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);

        final TextView header = root.findViewById(R.id.transaction_text);
        final TextView amount = root.findViewById(R.id.amount);
        final TextView category = root.findViewById(R.id.category);
        final TextView description = root.findViewById(R.id.description);

        final Bundle transactionBundle = this.getArguments();

        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                header.setText(s);
                amount.setText(transactionBundle.getString("amount"));
                description.setText(transactionBundle.getString("description"));
            }
        });

        return root;
    }

}
