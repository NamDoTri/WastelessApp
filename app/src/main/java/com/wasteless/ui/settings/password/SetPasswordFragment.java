package com.wasteless.ui.settings.password;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.wasteless.MainActivity;
import com.wasteless.R;
import com.wasteless.ui.settings.SettingsViewModel;

import java.util.List;
import java.util.zip.Inflater;

public class SetPasswordFragment extends Fragment {
    private PrivacyViewModel privacyViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_password, container, false);
        privacyViewModel = new ViewModelProvider(requireActivity()).get(PrivacyViewModel.class);

        PatternLockView patternView = root.findViewById(R.id.password_view);
        patternView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String currentPattern = PatternLockUtils.patternToString(patternView, pattern);
                if(privacyViewModel.getIsConfirming() == false){
                    privacyViewModel.setCurrentPassword(currentPattern);
                    privacyViewModel.setIsConfirming(true);
                    patternView.clearPattern();
                }else{
                    boolean isValid = privacyViewModel.validatePassword(currentPattern);
                    if(isValid){
                        privacyViewModel.savePassword();
                        privacyViewModel.setIsConfirming(false);
                        privacyViewModel.setCurrentPassword("");

                        PrivacyFragment privacyFragment = new PrivacyFragment();
                        FragmentTransaction transaction =getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, privacyFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        patternView.clearPattern();
                    }
                }
            }
            @Override
            public void onCleared() {}
        });

        return root;
    }
}
