package com.wasteless.ui.settings.backup;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.opencsv.CSVWriter;
import com.wasteless.roomdb.AppDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupViewModel extends AndroidViewModel {

    private String folderName = "backups";
    private String zipName = "backups.zip";
    private File folderLocation = new File(getApplication().getFilesDir().getAbsolutePath() + "/" + folderName);
    private File zipLocation = new File(getApplication().getFilesDir().getAbsolutePath() + "/" + zipName);

    public BackupViewModel(Application application) {
        super(application);
    }

    private void exportDataBaseIntoCSV(){
        //Get the database in readable form
        AppDatabase db = AppDatabase.getAppDatabase(getApplication());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

        //Create a folder for the backups
        File exportDir = new File(getApplication().getFilesDir().getAbsolutePath(), "backups");
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

    private void exportBackupsToZip(){
        //TODO: remove the backup folder after sending the backups to the cloud?
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
    }

    public Intent exportBackups(){
        exportDataBaseIntoCSV();
        exportBackupsToZip();

        Uri path = FileProvider.getUriForFile(getApplication(), getApplication().getApplicationContext().getPackageName() + ".provider", zipLocation);

        //Creating the email/cloud chooser through intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wasteless Backups");
        intent.putExtra(Intent.EXTRA_TEXT   , "Hey, your requested backups are attached to this message.");
        intent.putExtra(Intent.EXTRA_STREAM , path);

        return intent;
    }

}
