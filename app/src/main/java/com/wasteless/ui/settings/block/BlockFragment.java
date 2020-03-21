package com.wasteless.ui.settings.block;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class BlockFragment extends Fragment {
    private SettingsViewModel SettingsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View BlockFragment = inflater.inflate(R.layout.block_fragment, container, false);
        return BlockFragment;
    };

}
