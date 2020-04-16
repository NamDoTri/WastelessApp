package com.wasteless.ui.settings.password;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

public class PrivacyFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private PrivacyViewModel privacyViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        privacyViewModel = new ViewModelProvider(requireActivity()).get(PrivacyViewModel.class);

        ToggleButton enablePassword = ((ToggleButton)root.findViewById(R.id.enable_password_check));
        enablePassword.setChecked(privacyViewModel.getIsPasswordEnabled());

        enablePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.i("pattern", "password enabled");
                    SetPasswordFragment setPasswordFragment = new SetPasswordFragment();
                    FragmentTransaction transaction =getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, setPasswordFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    enablePassword.setChecked(false);
                }else{
                    Log.i("pattern", "password disabled");
                    privacyViewModel.removePassword();
                }
            }
        });

        return root;
    }
}
