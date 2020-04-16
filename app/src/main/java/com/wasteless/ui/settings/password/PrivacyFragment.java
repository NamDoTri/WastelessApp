package com.wasteless.ui.settings.password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class PrivacyFragment extends Fragment {
    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        return root;
    }
}
