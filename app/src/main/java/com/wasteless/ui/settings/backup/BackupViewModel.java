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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

    public Intent importBackups(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");

        return intent;
    }

    public void insertData(Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = getApplication().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
        ZipEntry zipEntry;

        //Creating a list for the tag_assoc inserts for later use
        List<String> tag_assocs = new ArrayList<>();

        //Getting the database in readable form
        AppDatabase db = AppDatabase.getAppDatabase(getApplication());
        SupportSQLiteDatabase sql_db = db.getOpenHelper().getReadableDatabase();

        try {
            //Going through the .csv files in the .zip folder one at a time
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
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
                if (fileName.equals("tag_assoc")) {
                    try {
                        while ((header = bufferedReader.readLine()) != null) {
                            StringBuilder sb = new StringBuilder(queryString1);
                            String[] str = header.split(",");
                            for (int i = 0; i < str.length; i++) {
                                if (i != str.length - 1) {
                                    sb.append(str[i] + ",");
                                } else {
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
                else {
                    sql_db.beginTransaction();
                    try {
                        while ((header = bufferedReader.readLine()) != null) {
                            StringBuilder sb = new StringBuilder(queryString1);
                            String[] str = header.split(",");
                            for (int i = 0; i < splitHeader.length; i++) {
                                //Checking if the header is longer than the string of data entries
                                //(used especially in the transactions)
                                if (str.length < splitHeader.length && i != splitHeader.length - 1) {
                                    sb.append(str[i] + ",");
                                }
                                if (str.length < splitHeader.length && i == splitHeader.length - 1) {
                                    sb.append("'" + "" + "'");
                                }
                                if (str.length == splitHeader.length && i != str.length - 1) {
                                    sb.append(str[i] + ",");
                                }
                                if (str.length == splitHeader.length && i == str.length - 1) {
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
        for (int i = 0; i < tag_assocs.size(); i++) {
            sql_db.execSQL(tag_assocs.get(i));
            Log.d("EXECUTED", tag_assocs.get(i));
        }
        sql_db.setTransactionSuccessful();
        sql_db.endTransaction();
    }

}
