package com.wasteless.ui.edit_transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.ui.transaction.TransactionViewModel;

public class EditTransactionFragment extends Fragment {

    TransactionViewModel transactionViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_add_new_transaction, container, false);

        final Bundle editTransactionBundle = this.getArguments();

        final Transaction transaction = transactionViewModel.getTransactionById(editTransactionBundle.getLong("id"));

        final EditText dateField = root.findViewById(R.id.date);
        //final Spinner categoryField = root.findViewById(R.id.category);
        final EditText sumField = root.findViewById(R.id.amount);
        final EditText tagsField = root.findViewById(R.id.add_tags);
        final EditText descriptionField = root.findViewById(R.id.description);
        final EditText sourceField = root.findViewById(R.id.source);

        dateField.setText(transaction.date);
        descriptionField.setText(transaction.description);
        sumField.setText(String.valueOf(transaction.amount));


        root.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = dateField.getText().toString();
                //String category = String.valueOf(categoryField.getSelectedItem());
                String sum = sumField.getText().toString().trim();
                //Float sum1 = Float.parseFloat(sumField.getText().toString().trim());
                String tags = tagsField.getText().toString();
                //String source = sourceField.getText().toString();
                String description = descriptionField.getText().toString();


                //Transaction transaction = new Transaction(date, Float.parseFloat(sum), description, Long.valueOf(1), false, category);
                TransactionRepository.getTransactionRepository(getContext()).update(transaction);

            }
        });
        return root;
    }
}
