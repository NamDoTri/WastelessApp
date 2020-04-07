package com.wasteless.ui.settings.bankAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class BankAccountFragment extends Fragment {
    private com.wasteless.ui.settings.SettingsViewModel SettingsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View BankAccountFragment = inflater.inflate(R.layout.bank_account_fragent, container, false);
        return BankAccountFragment;
    };
}
