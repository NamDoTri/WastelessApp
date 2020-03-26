package com.wasteless.ui.settings.newWallet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.settings.SettingsViewModel;

public class NewWalletFragment extends Fragment {
    private SettingsViewModel SettingsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View MeFragmentView = inflater.inflate(R.layout.new_wallet_fragment, container, false);
        MeFragmentView.findViewById(R.id.add_wallet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = MeFragmentView.findViewById(R.id.wallet_name);
                EditText initialValueField = MeFragmentView.findViewById(R.id.initial_value);
                String name = nameField.getText().toString();
                String initialValue = initialValueField.getText().toString().trim();
                if(v.getId() == R.id.add_wallet_button) {
                    Log.i("wallet", name +" "+initialValue);
                    AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
                    appDatabase.walletDao().insertAll(new Wallet(name, Float.parseFloat(initialValue)));

                }
            }
        });
        return MeFragmentView;
    }
}
