package com.wasteless.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.wasteless.ui.settings.block.BlockFragment;
import com.wasteless.ui.settings.help.HelpFragment;
import com.wasteless.ui.settings.me.GeneralFragment;
import com.wasteless.ui.settings.me.MeFragment;
import com.wasteless.ui.settings.me.NotificationsFragment;
import com.wasteless.ui.settings.privacy.PrivacyFragment;

public class SettingsFragment extends Fragment{

    private SettingsViewModel SettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        root.findViewById(R.id.me_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().alpha(0.3f).setDuration(200).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.animate().alpha(1f).setDuration(200).start();
                    default :
                        v.setAlpha(1f);
                }
                return false;
            }
        });
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
        root.findViewById(R.id.notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, notificationsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        root.findViewById(R.id.general_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralFragment generalFragment = new GeneralFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, generalFragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        root.findViewById(R.id.block_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            BlockFragment blockFragment = new BlockFragment();
                            FragmentTransaction transaction =getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, blockFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                    }
                }
        );
        root.findViewById(R.id.privacy_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrivacyFragment privacyFragment = new PrivacyFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, privacyFragment);
                        transaction.commit();
                    }
                }
        );
        root.findViewById(R.id.help_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HelpFragment helpFragment = new HelpFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, helpFragment);
                        transaction.commit();
                    }
                }
        );
        return root;
    }
}
