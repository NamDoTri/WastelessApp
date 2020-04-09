package com.wasteless.ui.settings;

import android.content.Intent;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.opencsv.CSVWriter;
import com.wasteless.R;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.ui.settings.bankAccount.BankAccountFragment;
import com.wasteless.ui.settings.block.BlockFragment;
import com.wasteless.ui.settings.help.HelpFragment;
import com.wasteless.ui.settings.newWallet.GeneralFragment;
import com.wasteless.ui.settings.newWallet.NewWalletFragment;
import com.wasteless.ui.settings.newWallet.NotificationsFragment;
import com.wasteless.ui.settings.privacy.PrivacyFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SettingsFragment extends Fragment{

    private SettingsViewModel SettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
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
                        exportDataBaseIntoCSV();
                        sendZip();
                    }
                }
        );
        return root;
    }

    public void exportDataBaseIntoCSV(){
        //TODO: move these to the view model!!

        //Get the database in readable form
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

        //Create a folder for the backups
        File exportDir = new File(getActivity().getFilesDir().getAbsolutePath(), "backups");
        if (!exportDir.exists()){
            exportDir.mkdirs();
        }
        //Get the names of the tables in the database
        List<String> tableNames = new ArrayList<>();
        Cursor tableListCursor = sql_db.query("SELECT name FROM sqlite_master WHERE type='table' " +
                "AND name NOT LIKE 'android_metadata' " +
                "AND name NOT LIKE 'sqlite_sequence' " +
                "AND name NOT LIKE 'room_master_table' ", null);
        //Here ^^ are the tables that we don't need to backup (not sure about the master table tho)
        while(tableListCursor.moveToNext()) {
            tableNames.add(tableListCursor.getString(0));
        }
        tableListCursor.close();

        //Loop through the tables and create .csv file for each table
        for (String tableName : tableNames){
            try {
                Cursor curCSV = sql_db.query("SELECT * FROM " + tableName,null);
                File file = new File(exportDir, tableName + ".csv");
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                csvWrite.writeNext(curCSV.getColumnNames());
                int columnAmount = curCSV.getColumnCount();
                String[] columnData = new String[columnAmount];
                while(curCSV.moveToNext()) {
                    for(int i=0; i < columnAmount; i++){
                        columnData[i] = curCSV.getString(i);
                    }
                    csvWrite.writeNext(columnData);
                }
                csvWrite.close();
                curCSV.close();
            }
            catch(Exception sqlEx) {
                Log.e("Error:", sqlEx.getMessage(), sqlEx);
            }
        }
    }

    private void sendZip(){
        //TODO: remove the backup folder after sending the backups to the cloud?

        //Get the location of backups and zip files
        String folderName = "backups";
        String zipName = "backups.zip";
        File folderLocation = new File(getActivity().getFilesDir().getAbsolutePath() + "/" + folderName);
        File zipLocation = new File(getActivity().getFilesDir().getAbsolutePath() + "/" + zipName);

        //Start streams to push the files into zip
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipLocation);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            File[] files = folderLocation.listFiles();
            for (File file : files) {
                byte[] buffer = new byte[1024];
                FileInputStream fileInputStream = new FileInputStream(file);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }
            zipOutputStream.close();
        } catch (IOException e) {
            Log.e("", e.getMessage());
        }

        //The path for the zipped folder
        Uri path = FileProvider.getUriForFile(getActivity(), getContext().getApplicationContext().getPackageName() + ".provider", zipLocation);

        //Creating the email/cloud chooser through intent
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("vnd.android.cursor.dir/email");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, "Wasteless Backups");
        i.putExtra(Intent.EXTRA_TEXT   , "Hey, your requested backups are attached to this message.");
        i.putExtra(Intent.EXTRA_STREAM , path);
        try {
            startActivity(Intent.createChooser(i, "Backup"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email/cloud clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
