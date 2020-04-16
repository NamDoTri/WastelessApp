package com.wasteless.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wasteless.R;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.ui.settings.backup.BackupViewModel;
import com.wasteless.ui.settings.bankAccount.BankAccountFragment;
import com.wasteless.ui.settings.block.BlockFragment;
import com.wasteless.ui.settings.help.HelpFragment;
import com.wasteless.ui.settings.newWallet.GeneralFragment;
import com.wasteless.ui.settings.newWallet.NewWalletFragment;
import com.wasteless.ui.settings.achievements.AchievementFragment;
import com.wasteless.ui.settings.password.PrivacyFragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SettingsFragment extends Fragment{

    private SettingsViewModel SettingsViewModel;
    private BackupViewModel backupViewModel;
    private static final int CHOOSE_FILE_REQUESTCODE = 1112;
    private static final int BACKUP_REQUESTCODE = 2223;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        backupViewModel = ViewModelProviders.of(this).get(BackupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        root.findViewById(R.id.add_wallet_button).setOnTouchListener(new View.OnTouchListener() {
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
        root.findViewById(R.id.bank_account_button).setOnTouchListener(new View.OnTouchListener() {
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
        root.findViewById(R.id.add_wallet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWalletFragment newWalletFragment = new NewWalletFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, newWalletFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        root.findViewById(R.id.bank_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankAccountFragment bankAccountFragment = new BankAccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, bankAccountFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        root.findViewById(R.id.acievement_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AchievementFragment achievementFragment = new AchievementFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.nav_host_fragment, achievementFragment);
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
        root.findViewById(R.id.privacy_button).setOnClickListener(v -> {
            PrivacyFragment privacyFragment = new PrivacyFragment();
            FragmentTransaction transaction =getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, privacyFragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
        root.findViewById(R.id.backup_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendZip();
                    }
                }
        );
        root.findViewById(R.id.import_data_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectZip();
                    }
                }
        );
        return root;
    }

    private void sendZip(){
        AlertDialog.Builder backupAlert = new AlertDialog.Builder(getActivity());
        backupAlert.setTitle("Backup");
        backupAlert.setMessage("Do you want your data to be saved as a .zip file and " +
                "to store it in a location of your choosing?");
        backupAlert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        backupAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = backupViewModel.exportBackups();
                try {
                    startActivityForResult(Intent.createChooser(i, "Backup"), BACKUP_REQUESTCODE);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There aren't any clients installed to handle your request.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backupAlert.show();
    }

    private void selectZip(){
        AlertDialog.Builder importAlert = new AlertDialog.Builder(getActivity());
        importAlert.setTitle("Import data");
        importAlert.setMessage("Do you want to import your data from the previously saved .zip file?" +
                " This action will also remove all the data that isn't in the backup file.");
        importAlert.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        importAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = backupViewModel.importBackups();
                try {
                    startActivityForResult(Intent.createChooser(i, "Choose a file"), CHOOSE_FILE_REQUESTCODE);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There aren't any clients installed to handle your request.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        importAlert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            return;
        }
        if(requestCode == CHOOSE_FILE_REQUESTCODE) {
            //Getting the provided uri and sending it to the view model
            Uri uri = data.getData();
            backupViewModel.insertData(uri);

            Toast.makeText(getContext(), "Data imported", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == BACKUP_REQUESTCODE){
            Toast.makeText(getContext(), "Backup successful", Toast.LENGTH_SHORT).show();
        }
    }
}