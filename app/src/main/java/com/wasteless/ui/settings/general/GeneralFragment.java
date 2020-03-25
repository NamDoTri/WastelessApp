package com.wasteless.ui.settings.newWallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class GeneralFragment extends Fragment {
    private SettingsViewModel SettingsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View GeneralFragment = inflater.inflate(R.layout.general_fragment, container, false);

        return GeneralFragment;
    }
}
