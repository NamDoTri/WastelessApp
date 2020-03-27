package com.wasteless.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.models.Transaction;

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

        Context context = getContext();
        CharSequence text = "ID is " + transactionBundle.getString("id");
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                header.setText(s);
                amount.setText(transactionBundle.getString("amount"));
                description.setText(transactionBundle.getString("description"));
            }
        });

        root.findViewById(R.id.transaction_edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                //transactionViewModel.delete();

                //Delete fragment OR function with confirmation goes here

                Context context = getContext();
                CharSequence text = "Delete clicked";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        return root;
    }

}
