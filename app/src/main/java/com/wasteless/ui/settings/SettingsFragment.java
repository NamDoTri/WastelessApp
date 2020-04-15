package com.wasteless.ui.settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
                        selectZip();
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

    private void selectZip(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, CHOOSE_FILE_REQUESTCODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));

        ZipEntry zipEntry;
        List<String> names = new ArrayList<>();
        try{
            while ((zipEntry = zipInputStream.getNextEntry()) != null){
                String fileNameWithExt = zipEntry.getName();
                int fileExtIndex = fileNameWithExt.lastIndexOf(".");
                String fileName = fileNameWithExt.substring(0, fileExtIndex);

                names.add(fileName);
                //Constructing a reader to go through the stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));

                /*testtttt
                String line;
                while ((line = bufferedReader.readLine())  != null) {
                    Log.d("lines should be here", line);
                }*/

                String header = null;
                try {
                    header = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert header != null;
                String[] splitHeader = header.split(",");

                //Getting the filename (same as table name + .csv)
                /*Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                String fileNameWithExt = cursor.getString(nameIndex);
                int fileExtIndex = fileNameWithExt.lastIndexOf(".");
                String fileName = fileNameWithExt.substring(0, fileExtIndex);*/

                //Constructing query
                String str1 = "INSERT INTO " + fileName + " (" + header + ") values (";
                String str2 = ");";

                //Getting the database
                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

                //Clearing the table to make sure that there aren't any problems with id's
                //TODO: replace this with some logic to check that the data can be applied
                sql_db.execSQL("delete from " + fileName);
                Log.d("CLEARED", fileName);

                if(fileName.equals("tag_assoc")){
                    sql_db.beginTransaction();
                    try{
                        while ((header = bufferedReader.readLine())  != null) {
                            StringBuilder sb = new StringBuilder(str1);
                            String[] str = header.split(",");
                            /*for (int i = 0; i < splitHeader.length; i++){
                                if( str.length == splitHeader.length && i != str.length - 1){
                                    sb.append(str[i] + ",");
                                }
                                if(str.length == splitHeader.length && i == str.length - 1){
                                    sb.append("NULL");
                                }
                            }*/
                            sb.append(str[0] + ",");
                            sb.append("NULL"  + ",");
                            sb.append(str[2]);
                            sb.append(str2);
                            Log.d("EXECUTE", sb.toString());
                            sql_db.execSQL(sb.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sql_db.setTransactionSuccessful();
                    sql_db.endTransaction();
                    Log.d("EXECUTED TO", fileName);
                }
                else{
                    sql_db.beginTransaction();
                    try{
                        while ((header = bufferedReader.readLine())  != null) {
                            StringBuilder sb = new StringBuilder(str1);
                            String[] str = header.split(",");
                            for (int i = 0; i < splitHeader.length; i++){
                                //Checking if the header is longer than the string of data entries,
                                //this should only be used in transactions since there are empty fields
                                //in "source" if the transaction is an expense
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
                            sb.append(str2);
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
        Toast.makeText(getContext(), names.get(1), Toast.LENGTH_SHORT).show();
    }

    /*private void selectFile(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, CHOOSE_FILE_REQUESTCODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: implement this check to make sure that the function runs only when user chooses a file
        if(data == null)
            return;
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                String filepath = data.getData().getPath();
        }

        //Getting the data from the uri through a stream
        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Constructing a reader to go through the stream
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String header = null;
        try {
            header = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert header != null;
        String[] splitHeader = header.split(",");

        //Getting the filename (same as table name + .csv)
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String fileNameWithExt = cursor.getString(nameIndex);
        int fileExtIndex = fileNameWithExt.lastIndexOf(".");
        String fileName = fileNameWithExt.substring(0, fileExtIndex);

        //Constructing query
        String str1 = "INSERT INTO " + fileName + " (" + header + ") values (";
        String str2 = ");";

        //Getting the database
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

        sql_db.beginTransaction();
        try{
            while ((header = bufferedReader.readLine())  != null) {
                StringBuilder sb = new StringBuilder(str1);
                String[] str = header.split(",");
                for (int i = 0; i < splitHeader.length; i++){
                    Log.d("str length", String.valueOf(str.length));
                    Log.d("header length", String.valueOf(splitHeader.length));
                    //Checking if the header is longer than the string of data entries,
                    //this should only be used in transactions since there are empty fields
                    //in "source" if the transaction is an expense
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
                sb.append(str2);
                sql_db.execSQL(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sql_db.setTransactionSuccessful();
        sql_db.endTransaction();
        Toast.makeText(getContext(), fileName, Toast.LENGTH_SHORT).show();
    }*/
}