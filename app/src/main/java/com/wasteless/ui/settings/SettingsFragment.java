package com.wasteless.ui.settings;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
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
import androidx.core.content.FileProvider;
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
import com.wasteless.ui.settings.newWallet.NotificationsFragment;
import com.wasteless.ui.settings.privacy.PrivacyFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingsFragment extends Fragment{

    private SettingsViewModel SettingsViewModel;
    private BackupViewModel backupViewModel;
    private static final int CHOOSE_FILE_REQUESTCODE = 1234;

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
                        selectFile();
                        /*HelpFragment helpFragment = new HelpFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, helpFragment);
                        transaction.commit();*/
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
        return root;
    }

    private void sendZip(){
        Intent i = backupViewModel.getBackups();
        try {
            startActivity(Intent.createChooser(i, "Backup"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email/cloud clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, CHOOSE_FILE_REQUESTCODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(data == null)
            return;
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                String filepath = data.getData().getPath();
                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();
                String tablename = "transactions";
                String columns = "transactionId, date, amount, description, wallet, isIncome, type, source";
                String str1 = "INSERT INTO " + tablename + " (" + columns + ") values(";
                String str2 = ");";
                FileReader file = null;
                try {
                    file = new FileReader(filepath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BufferedReader buffer = new BufferedReader(file);
                    ContentValues contentValues = new ContentValues();
                    String line = "";
                    sql_db.beginTransaction();

                    while (true) {
                        try {
                            if (!((line = buffer.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
                        sb.append("'" + str[0] + "',");
                        sb.append(str[1] + "',");
                        sb.append(str[2] + "',");
                        sb.append(str[3] + "'");
                        sb.append(str[4] + "'");
                        sb.append(str2);
                        sql_db.execSQL(sb.toString());
                    }
                    sql_db.setTransactionSuccessful();
                    sql_db.endTransaction();

        }*/

        String filePath = null;
        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String tableName ="transactions";
        String columns = "transactionId, date, amount, description, wallet, isIncome, type, source";
        String str1 = "INSERT INTO " + tableName + " (" + columns + ") values (";
        String str2 = ");";

        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();


        sql_db.beginTransaction();
        try{
            while ((line = bufferedReader.readLine())  != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = line.split(",");
                sb.append(str[0] + ",");
                sb.append(str[1] + ",");
                sb.append(str[2] + ",");
                sb.append(str[3] + ",");
                sb.append(str[4] + ",");
                sb.append(str[5] + ",");
                sb.append(str[6] + ",");
                sb.append("'" + "asd" + "'");
                sb.append(str2);
                sql_db.execSQL(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sql_db.setTransactionSuccessful();
        sql_db.endTransaction();
        /*String line = "";
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 8) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(8);
                cv.put("transactionId", columns[0].trim());
                cv.put("date", columns[1].trim());
                cv.put("amount", columns[2].trim());
                cv.put("description", columns[3].trim());
                cv.put("wallet", columns[4].trim());
                cv.put("isIncome", columns[5].trim());
                cv.put("type", columns[6].trim());
                cv.put("source", columns[7].trim());
                sql_db.insert("transactions", 0, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

