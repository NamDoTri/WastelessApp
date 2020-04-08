package com.wasteless.ui.settings.newWallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.add_transaction.AddTransactionFragment;
import com.wasteless.ui.settings.SettingsFragment;
import com.wasteless.ui.settings.SettingsViewModel;

public class NewWalletFragment extends Fragment {
    private SettingsViewModel settingsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View MeFragmentView = inflater.inflate(R.layout.new_wallet_fragment, container, false);
        MeFragmentView.findViewById(R.id.add_wallet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = MeFragmentView.findViewById(R.id.wallet_name);
                EditText initialValueField = MeFragmentView.findViewById(R.id.initial_value);
                String name = nameField.getText().toString();
                String initialValue = initialValueField.getText().toString().trim();

                if(v.getId() == R.id.add_wallet_button) {
                    settingsViewModel.insertWallet(name, Double.valueOf(initialValue), false);
                    successMessage();
                }
            }
            private void successMessage() {
                // Pop-up message
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Done");
                alertDialog.setMessage("Succesfully added");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingsFragment settingsFragment = new SettingsFragment();
                                FragmentTransaction changeTheScreen = getFragmentManager().beginTransaction();
                                changeTheScreen.replace(R.id.nav_host_fragment, settingsFragment);
                                changeTheScreen.addToBackStack(null);
                                changeTheScreen.commit();
                            }
                        });
                alertDialog.show();
            }
        });
        return MeFragmentView;
    }
}
