package com.example.techstore.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.techstore.model.Product;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.User;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public String getUserName() {
        return sharedPrefManager.getUserName();
    }

    public String getImg() {
        return sharedPrefManager.getImg();
    }

    public void addProduct(ProductInCart product, Callback callback){
        String email = sharedPrefManager.getEmail();
        List<ProductInCart> listProduct = new ArrayList<>();
        if (email != null) {
//            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
//                            .get()
//                                    .addOnCompleteListener(getProducts -> {
//                                        if (getProducts.isSuccessful() && !getProducts.getResult().isEmpty()) {
//                                            for (DocumentSnapshot documentSnapshot : getProducts.getResult().getDocuments()) {
//                                                if (documentSnapshot.getString(Constants.))
//                                            }
//                                        }
//                                    })

            Map<String, Object> products = new HashMap<>();
            products.put(Constants.KEY_SHARE_PRODUCT, FieldValue.arrayUnion(product));
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .set(products, SetOptions.merge())
                    .addOnSuccessListener(unused -> callback.onResult(true))
                    .addOnFailureListener(e -> callback.onResult(false));
        }
    }
}
