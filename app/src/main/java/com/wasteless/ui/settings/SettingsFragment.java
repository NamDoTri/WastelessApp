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
import com.wasteless.ui.settings.block.BlockFragment;
import com.wasteless.ui.settings.help.HelpFragment;
import com.wasteless.ui.settings.newWallet.GeneralFragment;
import com.wasteless.ui.settings.newWallet.NewWalletFragment;
import com.wasteless.ui.settings.newWallet.NotificationsFragment;
import com.wasteless.ui.settings.privacy.PrivacyFragment;

import java.io.File;
import java.io.FileWriter;

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
                        sendCSV();
                    }
                }
        );
        return root;
    }

    public void exportDataBaseIntoCSV(){
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        File exportDir = new File(getActivity().getFilesDir().getAbsolutePath(), "");

        if (!exportDir.exists()){
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "backup.csv");

        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();//here create a method ,and return SQLiteDatabaseObject.getReadableDatabase();
            Cursor curCSV = sql_db.query("SELECT * FROM transactions",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String arrStr[] ={
                    curCSV.getString(0),
                    curCSV.getString(1),
                    curCSV.getString(2),
                    curCSV.getString(3),
                    curCSV.getString(4),
                    curCSV.getString(5),
                    curCSV.getString(6),
                    curCSV.getString(7)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("Error:", sqlEx.getMessage(), sqlEx);
        }
    }

    public void sendCSV(){
        String filename = "backup.csv";
        File fileLocation = new File(getActivity().getFilesDir().getAbsolutePath() + "/" + filename);

        Uri path = FileProvider.getUriForFile(getActivity(), getContext().getApplicationContext().getPackageName() + ".provider", fileLocation);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("vnd.android.cursor.dir/email");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, "Your Requested Transaction Backup");
        i.putExtra(Intent.EXTRA_TEXT   , "Hey, your requested backup is attached to this message.");
        i.putExtra(Intent.EXTRA_STREAM , path);
        try {
            startActivity(Intent.createChooser(i, "Sending mail..."));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
