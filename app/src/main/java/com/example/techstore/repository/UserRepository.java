package com.example.techstore.repository;

import android.content.Context;
import com.example.techstore.model.User;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

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

    public interface Callback {
        void onResult(boolean result);
    }

    public void checkEmailisExits(String email, Callback callback) {
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            callback.onResult(true);
                        } else {
                            callback.onResult(false);
                        }
                    }
                });
    }

    public void loginSuccess(User user, boolean isSaveInf, Callback callback) {
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_EMAIL, user.getEmail())
                .whereEqualTo(Constants.KEY_PASSWORD, user.getPassword())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot userResult = task.getResult().getDocuments().get(0);
                        sharedPrefManager.saveEmail(userResult.getString(Constants.KEY_EMAIL));
                        sharedPrefManager.savePassword(userResult.getString(Constants.KEY_PASSWORD));
                        sharedPrefManager.setIsLogin(true);
                        sharedPrefManager.setSaveInf(isSaveInf);
                        sharedPrefManager.saveUsername(userResult.getString(Constants.KEY_USERNAME));
                        sharedPrefManager.saveImg(userResult.getString(Constants.KEY_IMG));
                        callback.onResult(true);
                    } else {
                        callback.onResult(false);
                    }
                });
    }

    public void signupSuccess(Map<String, String> user, Callback result) {
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .add(user)
                .addOnSuccessListener(documentReference -> result.onResult(true))
                .addOnFailureListener(e -> result.onResult(false));
    }
}
