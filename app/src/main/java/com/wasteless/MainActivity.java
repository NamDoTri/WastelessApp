package com.wasteless;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.wasteless.roomdb.AppDatabase;
import com.wasteless.roomdb.entities.Wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private static final String PASSWORD_FILENAME = "wastelessPassword";
    private static String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getAppDatabase(this);
        Log.i("Database", "Wallet " + String.valueOf(db.walletDao().getAll()));
        Log.i("Database", "Expense " + String.valueOf(db.transactionDao().getAllExpenses()));
        Log.i("Database", "Income " + String.valueOf(db.transactionDao().getAllIncomes()));

        setContentView(R.layout.activity_main);

        // check if any password file saved in internal storage
        try{
            FileInputStream fis = getApplicationContext().openFileInput(PASSWORD_FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                password = stringBuilder.toString();
                Log.i("Pattern", "Stored password: " + password);
            }
        }catch (Exception e){
            Log.i("pattern", "No password file found.");
            e.printStackTrace();
        }

        PatternLockView patternView = findViewById(R.id.password_view);
        ConstraintLayout defaultMain = findViewById(R.id.default_main);

        if (password != null) {
            patternView.setVisibility(View.VISIBLE);
            defaultMain.setVisibility(View.GONE);
        }
        patternView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String currentPattern = PatternLockUtils.patternToString(patternView, pattern);
                if(validatePassword(currentPattern) == true){
                    patternView.setVisibility(View.GONE);
                    defaultMain.setVisibility(View.VISIBLE);
                }else{
                    patternView.clearPattern();
                }
            }
            @Override
            public void onCleared() {
                // TODO: limit number of attempts
            }
        });



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add_new_transaction ,R.id.navigation_wallets_manager, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseApp.initializeApp(this);
    }

    public static String getPasswordFilename(){
        return PASSWORD_FILENAME;
    }
    private boolean validatePassword(String newPassword){
        return newPassword.trim().equalsIgnoreCase(password.trim());
    }
    public static String getStoredPassword(){
        return password;
    }
}
