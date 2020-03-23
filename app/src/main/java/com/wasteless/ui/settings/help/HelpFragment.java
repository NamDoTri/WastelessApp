package com.wasteless.ui.settings.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class HelpFragment extends Fragment {
    SettingsViewModel SettingsViewModel;
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View HelpView = inflater.inflate(R.layout.help_fragment, container, false);
        return  HelpView;
    }
}
