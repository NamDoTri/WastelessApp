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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private static String currentPassword = "012";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getAppDatabase(this);
        Log.i("Database", "Wallet " + String.valueOf(db.walletDao().getAll()));
        Log.i("Database", "Expense " + String.valueOf(db.transactionDao().getAllExpenses()));
        Log.i("Database", "Income " + String.valueOf(db.transactionDao().getAllIncomes()));

        setContentView(R.layout.activity_main);

        PatternLockView patternView = findViewById(R.id.password_view);
        ConstraintLayout defaultMain = findViewById(R.id.default_main);
        patternView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String currentPattern = PatternLockUtils.patternToString(patternView, pattern);
                if(MainActivity.validatePassword(currentPattern)){
                    patternView.setVisibility(View.GONE);
                    defaultMain.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCleared() {}
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add_new_transaction ,R.id.navigation_history, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseApp.initializeApp(this);
    }

    public static String getCurrentPassword(){
        return currentPassword;
    }
    public static boolean validatePassword(String newPassword){
        return newPassword.equalsIgnoreCase(currentPassword);
    }
}
