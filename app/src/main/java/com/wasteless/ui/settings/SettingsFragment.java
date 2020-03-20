package com.wasteless.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.ui.settings.me.MeFragment;

public class SettingsFragment extends Fragment{

    private SettingsViewModel SettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
//        final TextView textView = root.findViewById(R.id.text_settings);
//        SettingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        root.findViewById(R.id.me_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeFragment meFragment = new MeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, meFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return root;
    }
}
