package com.wasteless.ui.edit_transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wasteless.R;

public class EditTransactionFragment extends Fragment {

    //private AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_add_new_transaction, container, false);

        final Bundle transactionBundle = this.getArguments();

        final EditText dateField = root.findViewById(R.id.date);
        //final EditText categoryField = root.findViewById(R.id.category);
        final EditText sumField = root.findViewById(R.id.sum);
        final EditText tagsField = root.findViewById(R.id.tags);
        final EditText descriptionField = root.findViewById(R.id.description);
        final EditText sourceField = root.findViewById(R.id.source);

        descriptionField.setText(transactionBundle.getString("description"));
        sumField.setText(transactionBundle.getString("amount"));

        return root;
    }
}
