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
import com.google.gson.Gson;

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

    public interface ListCallback {
        void onResult(List<String> result);
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

    public void addOrUpdateProduct(ProductInCart product, Callback callback){

        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                        Gson gson = new Gson();
                        boolean productExist = false;

                        List<String> correctList = new ArrayList<>();
                        if (currentList != null) {
                            for (String strProductInCart : currentList) {
                                ProductInCart productInCart = gson.fromJson(strProductInCart, ProductInCart.class);
                                if (productInCart.equals(product)) {
                                    productExist = true;
                                    productInCart.setQuantity(productInCart.getQuantity() + product.getQuantity());
                                }
                                String strCorrectProduct = gson.toJson(productInCart);
                                correctList.add(strCorrectProduct);
                            }
                        }

                        if (!productExist) {
                            String productInCart = gson.toJson(product);
                            correctList.add(productInCart);
                        }

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put(Constants.KEY_SHARE_PRODUCT, correctList);

                        firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                                .document(email)
                                .set(updateData, SetOptions.merge())
                                .addOnSuccessListener(unused -> callback.onResult(true))
                                .addOnFailureListener(e -> callback.onResult(false));
                    })
                    .addOnFailureListener(e -> callback.onResult(false));
        }
    }

    public void getProductInCart(ListCallback callback) {
        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                            callback.onResult(currentList);
                        }
                    });
        }
    }
}
