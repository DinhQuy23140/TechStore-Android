package com.example.techstore.repository;

import android.content.Context;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.User;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
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
    private final SharedPrefManager sharedPrefManager;
    FirebaseFirestore firebaseFirestore;
    Gson gson;

    public UserRepository(Context context) {
        this.sharedPrefManager = new SharedPrefManager(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
        gson = new Gson();
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
                        callback.onResult(!task.getResult().isEmpty());
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
                        sharedPrefManager.savePhone(userResult.getString(Constants.KEY_PHONE));
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

    public String getEmail() {return sharedPrefManager.getEmail();}

    public String getImg() {
        return sharedPrefManager.getImg();
    }

    public String getPhone() {return sharedPrefManager.getPhone();}

    public String getDoB() {return sharedPrefManager.getDoB();}

    public void updateUser(Map<String, Object> user, Callback callback) {
        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                    .whereEqualTo(Constants.KEY_EMAIL, email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference().update(user)
                                    .addOnSuccessListener(unused -> {
                                        sharedPrefManager.saveUsername(Objects.requireNonNull(user.get(Constants.KEY_USERNAME)).toString());
                                        sharedPrefManager.saveImg(Objects.requireNonNull(user.get(Constants.KEY_IMG)).toString());
                                        sharedPrefManager.savePhone(Objects.requireNonNull(user.get(Constants.KEY_PHONE)).toString());
                                        sharedPrefManager.saveEmail(Objects.requireNonNull(user.get(Constants.KEY_EMAIL)).toString());
                                        sharedPrefManager.saveDoB(Objects.requireNonNull(user.get(Constants.KEY_DOB)).toString());
                                        callback.onResult(true);
                                    })
                                    .addOnFailureListener(e -> callback.onResult(false));
                        } else {
                            callback.onResult(false);
                        }
                    })
                    .addOnFailureListener(e -> callback.onResult(false));
        }
    }

    public void addOrUpdateProduct(ProductInCart product, Callback callback){

        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
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
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                        callback.onResult(currentList);
                    });
        }
    }

    public void deleteProductInCart(ProductInCart product) {
        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                        List<String> correctList = new ArrayList<>();
                        if (currentList != null) {
                            for (String strProductInCart : currentList) {
                                ProductInCart productInCart = gson.fromJson(strProductInCart, ProductInCart.class);
                                if (!productInCart.equals(product)) {
                                    String strCorrectProduct = gson.toJson(productInCart);
                                    correctList.add(strCorrectProduct);
                                }
                            }
                        }

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put(Constants.KEY_SHARE_PRODUCT, correctList);

                        firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                                .document(email)
                                .set(updateData, SetOptions.merge());
                    });
        }
    }

    public void clearCart(List<ProductInCart> listProduct) {
        String email = sharedPrefManager.getEmail();
        if (!email.isEmpty()) {
            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                    .document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                        List<ProductInCart> correctList = new ArrayList<ProductInCart>();
                        for (String product: currentList) {
                            ProductInCart productInCart = gson.fromJson(product, ProductInCart.class);
                            correctList.add(productInCart);
                        }
                        correctList.removeAll(listProduct);
                        if (!correctList.isEmpty()) {
                            List<String> correctListString = new ArrayList<>();
                            for (ProductInCart productInCart : correctList) {
                                String strCorrectProduct = gson.toJson(productInCart);
                                correctListString.add(strCorrectProduct);
                            }
                            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                                    .document(email)
                                    .update(Constants.KEY_SHARE_PRODUCT, correctListString);
                        } else {
                            firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                                    .document(email)
                                    .delete();
                        }
                    });

        }
    }

    public void updateQuantityProduct(ProductInCart product) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> currentList = (List<String>) documentSnapshot.get(Constants.KEY_SHARE_PRODUCT);
                    List<String> correctList = new ArrayList<>();
                    if (currentList != null) {
                        for (String strProductInCart : currentList) {
                            ProductInCart productInCart = gson.fromJson(strProductInCart, ProductInCart.class);
                            if (productInCart.equals(product)) {
                                productInCart.setQuantity(product.getQuantity());
                            }
                            String strCorrectProduct = gson.toJson(productInCart);
                            correctList.add(strCorrectProduct);
                        }
                    }

                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put(Constants.KEY_SHARE_PRODUCT, correctList);
                    firebaseFirestore.collection(Constants.KEY_COLLECTION_CART)
                            .document(email)
                            .set(updateData, SetOptions.merge());
                });
    }

    public void addSearch(String search) {
        String email = sharedPrefManager.getEmail();
        Map<String ,Object> mapSearch = new HashMap<>();
        mapSearch.put(search, FieldValue.serverTimestamp());
        firebaseFirestore.collection(Constants.KEY_COLLECTION_SEARCH)
                .document(email)
                .set(mapSearch, SetOptions.merge());
    }

    public void getSearch(ListCallback callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_SEARCH)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> listSearch = new ArrayList<>();
                    Map<String, Object> currentList = documentSnapshot.getData();
                    for (String key : currentList.keySet()) {
                        listSearch.add(key);
                    }
                    callback.onResult(listSearch);
                });
    }

    public void deleteSearch(String search) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_SEARCH)
                .document(email)
                .update(search, FieldValue.delete());
    }
}
