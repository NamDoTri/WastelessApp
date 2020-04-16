package com.wasteless.ui.settings.password;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.MainActivity;

public class PrivacyViewModel extends AndroidViewModel {
    private boolean isConfirming = false;
    private String currentPassword = "";

    public PrivacyViewModel(Application application){
        super(application);
    }

    public boolean getIsConfirming(){
        return this.isConfirming;
    }

    public void setIsConfirming(boolean b){
        this.isConfirming = b;
    }

    public boolean validatePassword(String newPassword){
        return newPassword.equalsIgnoreCase(this.currentPassword);
    }

    public void setCurrentPassword(String newPassword){
        this.currentPassword = newPassword;
    }

}
