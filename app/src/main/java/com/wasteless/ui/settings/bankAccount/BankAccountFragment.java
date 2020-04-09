package com.wasteless.ui.settings.bankAccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class BankAccountFragment extends Fragment {
    private com.wasteless.ui.settings.SettingsViewModel SettingsViewModel;
    private com.wasteless.ui.settings.bankAccount.BankAccountViewModel BankAccountViewModel;
    private View bankButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        View BankAccountFragment = inflater.inflate(R.layout.bank_account_fragment, container, false);
        TextView resultText = BankAccountFragment.findViewById(R.id.accounts);
        bankButton = BankAccountFragment.findViewById(R.id.connect_button);
        EditText username = BankAccountFragment.findViewById(R.id.username);
        EditText password = BankAccountFragment.findViewById(R.id.password);

        bankButton.setOnClickListener(v -> {
            if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0){
                BankAccountViewModel.requestToOP(resultText);
                message("Successfully added");
            } else {
                message("Please write you password and username");
            }

        });
        return BankAccountFragment;
    };
    private void message(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Message");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
