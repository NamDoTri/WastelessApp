package com.wasteless.ui.settings;

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
import com.wasteless.ui.settings.newWallet.NotificationsFragment;
import com.wasteless.ui.settings.privacy.PrivacyFragment;

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
        Intent i = backupViewModel.exportBackups();
        try {
            startActivity(Intent.createChooser(i, "Backup"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There aren't any clients installed to handle your request.", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectZip(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/zip");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        try {
            startActivityForResult(chooseFile, CHOOSE_FILE_REQUESTCODE);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There aren't any clients installed to handle your request.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO: implement this check to make sure that the function runs only when user chooses a file
        if(data == null)
            return;
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:

                //Getting data from the provided uri and starting an inputstream to be able to read it
                Uri uri = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContext().getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
                ZipEntry zipEntry;

                //Creating a list for the tag_assoc inserts for later use
                List<String> tag_assocs = new ArrayList<>();

                //Getting the database in readable form
                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

                try{
                    //Going through the .csv files in the .zip folder one at a time
                    while ((zipEntry = zipInputStream.getNextEntry()) != null){
                        String fileNameWithExt = zipEntry.getName();
                        int fileExtIndex = fileNameWithExt.lastIndexOf(".");
                        String fileName = fileNameWithExt.substring(0, fileExtIndex);

                        //Constructing a reader to go through the stream
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));

                        //Getting the first row of data for later use
                        String header = null;
                        try {
                            header = bufferedReader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert header != null;
                        String[] splitHeader = header.split(",");

                        //Constructing a base for query
                        String queryString1 = "INSERT INTO " + fileName + " (" + header + ") values (";
                        String queryString2 = ");";

                        //Clearing the table to make sure that there aren't any problems with id's
                        //TODO: replace this with some logic to check that the data can be applied?
                        sql_db.execSQL("delete from " + fileName);
                        Log.d("CLEARED", fileName);

                        //Storing the tag_assoc inserts temporarily in an arraylist because of the foreign key (transactionId)
                        if(fileName.equals("tag_assoc")){
                            try{
                                while ((header = bufferedReader.readLine())  != null) {
                                    StringBuilder sb = new StringBuilder(queryString1);
                                    String[] str = header.split(",");
                                    for(int i = 0; i < str.length; i++){
                                        if(i != str.length - 1){
                                            sb.append(str[i] + ",");
                                        }
                                        else{
                                            sb.append(str[i]);
                                        }
                                    }
                                    sb.append(queryString2);
                                    Log.d("EXECUTE", sb.toString());
                                    tag_assocs.add(sb.toString());
                                    //sql_db.execSQL(sb.toString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //Inserting the rest of the tables
                        else{
                            sql_db.beginTransaction();
                            try{
                                while ((header = bufferedReader.readLine())  != null) {
                                    StringBuilder sb = new StringBuilder(queryString1);
                                    String[] str = header.split(",");
                                    for (int i = 0; i < splitHeader.length; i++){
                                        //Checking if the header is longer than the string of data entries
                                        //(used especially in the transactions)
                                        if( str.length < splitHeader.length && i != splitHeader.length - 1){
                                            sb.append(str[i] + ",");
                                        }
                                        if( str.length < splitHeader.length && i == splitHeader.length - 1){
                                            sb.append("'" + "" + "'");
                                        }
                                        if( str.length == splitHeader.length && i != str.length - 1){
                                            sb.append(str[i] + ",");
                                        }
                                        if(str.length == splitHeader.length && i == str.length - 1){
                                            sb.append(str[i]);
                                        }
                                    }
                                    sb.append(queryString2);
                                    sql_db.execSQL(sb.toString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sql_db.setTransactionSuccessful();
                            sql_db.endTransaction();
                            Log.d("EXECUTED TO", fileName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Executing the tag_assoc inserts now since the transactions have to be in the
                //table before these, otherwise the foreign key doesn't connect
                sql_db.beginTransaction();
                for (int i = 0; i < tag_assocs.size(); i++){
                    sql_db.execSQL(tag_assocs.get(i));
                    Log.d("EXECUTED", tag_assocs.get(i));
                }
                sql_db.setTransactionSuccessful();
                sql_db.endTransaction();

                Toast.makeText(getContext(), "Data imported", Toast.LENGTH_SHORT).show();
        }
    }
}