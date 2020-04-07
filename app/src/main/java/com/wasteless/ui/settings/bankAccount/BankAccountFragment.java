package com.wasteless.ui.settings.bankAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        BankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        View BankAccountFragment = inflater.inflate(R.layout.bank_account_fragment, container, false);
        TextView resultText = BankAccountFragment.findViewById(R.id.accounts);
        bankButton = BankAccountFragment.findViewById(R.id.connect_button);
        bankButton.setOnClickListener(v -> {
            BankAccountViewModel.requestToOP(resultText);
        });
        return BankAccountFragment;
    };
}
