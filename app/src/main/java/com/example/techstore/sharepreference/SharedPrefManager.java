package com.example.techstore.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.techstore.untilities.Constants;

public class SharedPrefManager {
    private SharedPreferences sharedPreferences;

    public SharedPrefManager (Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void savaeUserName(String username) {
        sharedPreferences.edit().putString(Constants.KEY_USERNAME, username).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(Constants.KEY_USERNAME, "");
    }

    public void saveEmail(String email) {
        sharedPreferences.edit().putString(Constants.KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Constants.KEY_EMAIL, "");
    }

    public void savePassword(String password) {
        sharedPreferences.edit().putString(Constants.KEY_PASSWORD, password).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(Constants.KEY_PASSWORD, "");
    }

    public void setIsLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(Constants.KEY_IS_LOGIN, isLogin).apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGIN, false);
    }

    public void setSaveInf(boolean isSaveInf) {
        sharedPreferences.edit().putBoolean(Constants.KEY_IS_SAVE_INF, isSaveInf).apply();
    }

    public boolean isSaveInf() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_SAVE_INF, false);
    }

    public void saveUsername(String username) {
        sharedPreferences.edit().putString(Constants.KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.KEY_USERNAME, "");
    }

    public void saveImg(String img) {
        sharedPreferences.edit().putString(Constants.KEY_IMG, img).apply();
    }

    public String getImg() {
        return sharedPreferences.getString(Constants.KEY_IMG, "");
    }

    public void savePhone(String phone) {
        sharedPreferences.edit().putString(Constants.KEY_PHONE, phone).apply();
    }

    public String getPhone() {
        return sharedPreferences.getString(Constants.KEY_PHONE, "");
    }
}
