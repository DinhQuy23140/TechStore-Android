package com.example.techstore.repository;

import android.content.Context;

import com.example.techstore.model.User;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private SharedPrefManager sharedPrefManager;
    FirebaseFirestore firebaseFirestore;

    public UserRepository(Context context) {
        this.sharedPrefManager = new SharedPrefManager(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager;
    }

    public void setSharedPrefManager(SharedPrefManager sharedPrefManager) {
        this.sharedPrefManager = sharedPrefManager;
    }

    public interface LoginCallback {
        void onResult(boolean result);
    }

    public void loginSuccess(User user, boolean isSaveInf, LoginCallback callback) {
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_EMAIL, user.getEmail())
                .whereEqualTo(Constants.KEY_PASSWORD, user.getPassword())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        sharedPrefManager.saveEmail(user.getEmail());
                        sharedPrefManager.savePassword(user.getPassword());
                        sharedPrefManager.setIsLogin(true);
                        sharedPrefManager.setSaveInf(isSaveInf);
                        callback.onResult(true);
                    } else {
                        callback.onResult(false);
                    }
                });
    }
}
