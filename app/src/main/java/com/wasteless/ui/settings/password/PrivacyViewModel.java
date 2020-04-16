package com.wasteless.ui.settings.password;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;

import com.wasteless.MainActivity;

import java.io.File;
import java.io.FileOutputStream;

public class PrivacyViewModel extends AndroidViewModel {
    private boolean isPasswordEnabled;
    private boolean isConfirming = false;
    private String currentPassword = "";

    public PrivacyViewModel(Application application){
        super(application);
        isPasswordEnabled = MainActivity.getStoredPassword() == null ? false : true;
    }
    public boolean getIsPasswordEnabled(){
        return isPasswordEnabled;
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

    void savePassword(){
        String filename = MainActivity.getPasswordFilename();
        try (FileOutputStream fos = getApplication().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(currentPassword.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    boolean removePassword(){
        File passwordFile = new File(getApplication().getApplicationContext().getFilesDir(), MainActivity.getPasswordFilename());
        try{
            passwordFile.delete();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
