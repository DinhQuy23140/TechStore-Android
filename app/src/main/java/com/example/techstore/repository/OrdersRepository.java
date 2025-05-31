package com.example.techstore.repository;
import android.content.Context;

import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.local.OverlayedDocument;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersRepository {
    SharedPrefManager sharedPrefManager;
    FirebaseFirestore firebaseFirestore;
    Context context;
    Gson gson;

    public OrdersRepository(Context context) {
        this.context = context;
        this.sharedPrefManager = new SharedPrefManager(context);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        gson = new Gson();
    }

    public interface Callback {
        void onResult(boolean result);
    }

    public interface ListCallback {
        void onResult(List<String> result);
    }

    public interface OrdersCallBack {
        void onResult(List<ProductOrders> result);
    }

    public void addOrders(ProductOrders productOrders, Callback callback) {
        String email = sharedPrefManager.getEmail();
        String strOrders = gson.toJson(productOrders);
        Map<String, Object> addOrders = new HashMap<>();
        addOrders.put(Constants.KEY_NAME_FILED_ORDERS, FieldValue.arrayUnion(strOrders));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .set(addOrders, SetOptions.merge())
                .addOnSuccessListener(OverlayedDocument -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void deleteOrders(ProductOrders productOrders) {
        String email = sharedPrefManager.getEmail();
        String strOrders = gson.toJson(productOrders);
        Map<String, Object> addOrders = new HashMap<>();
        addOrders.put(Constants.KEY_NAME_FILED_ORDERS, FieldValue.arrayRemove(strOrders));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .set(addOrders, SetOptions.merge());
    }

    public void addOrdersComplete(ProductOrders productOrders) {
        String email = sharedPrefManager.getEmail();
        String strOrders = gson.toJson(productOrders);
        Map<String, Object> addOrders = new HashMap<>();
        addOrders.put(Constants.KEY_COLLECTION_ORDER_COMPLETE, FieldValue.arrayUnion(strOrders));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .set(addOrders, SetOptions.merge());
    }

    public void getOrders(ListCallback callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_NAME_FILED_ORDERS);
                    callback.onResult(result);
                })
                .addOnFailureListener(error -> callback.onResult(null));
    }

    public void getCompleteOrders(ListCallback callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_COLLECTION_ORDER_COMPLETE);
                    callback.onResult(result);
                })
                .addOnFailureListener(error -> callback.onResult(null));
    }

    public void addOrdersCancel(ProductOrders productOrders) {
        String email = sharedPrefManager.getEmail();
        String strOrders = gson.toJson(productOrders);
        Map<String, Object> addOrders = new HashMap<>();
        addOrders.put(Constants.KEY_COLLECTION_ORDER_CANCEL, FieldValue.arrayUnion(strOrders));
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .set(addOrders, SetOptions.merge());
    }

    public void getOrdersCancel(ListCallback callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_COLLECTION_ORDER_CANCEL);
                    callback.onResult(result);
                })
                .addOnFailureListener(error -> callback.onResult(null));
    }

    public void getOrdersByTitle(String keySearch, OrdersCallBack callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_NAME_FILED_ORDERS);
                    if (result != null && !result.isEmpty()) {
                        List<ProductOrders> listOrders = new ArrayList<>();
                        for (String item : result) {
                            ProductOrders productOrders = gson.fromJson(item, ProductOrders.class);
                            for (ProductInCart productInCart : productOrders.getProducts()) {
                                if (productInCart.getTitle().toLowerCase().contains(keySearch.toLowerCase())) {
                                    listOrders.add(productOrders);
                                    break;
                                }
                            }
                        }
                        callback.onResult(listOrders);
                    } else callback.onResult(null);
                })
                .addOnFailureListener(error -> callback.onResult(null));
    }

    public void getOrdersCompleteByTitle(String keySearch, OrdersCallBack ordersCallBack) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_COLLECTION_ORDER_COMPLETE);
                    if (result != null && !result.isEmpty()) {
                        List<ProductOrders> listOrders = new ArrayList<>();
                        for (String item : result) {
                            ProductOrders productOrders = gson.fromJson(item, ProductOrders.class);
                            for (ProductInCart productInCart : productOrders.getProducts()) {
                                if (productInCart.getTitle().toLowerCase().contains(keySearch.toLowerCase())) {
                                    listOrders.add(productOrders);
                                    break;
                                }
                            }
                        }
                        ordersCallBack.onResult(listOrders);
                    } else ordersCallBack.onResult(null);
                })
                .addOnFailureListener(error -> ordersCallBack.onResult(null));
    }

    public void getOrdersCancelByTitle(String keySearch, OrdersCallBack ordersCallBack) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_ORDER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> result = (List<String>) documentSnapshot.get(Constants.KEY_COLLECTION_ORDER_CANCEL);
                    if (result != null && !result.isEmpty()) {
                        List<ProductOrders> listOrders = new ArrayList<>();
                        for (String item : result) {
                            ProductOrders productOrders = gson.fromJson(item, ProductOrders.class);
                            for (ProductInCart productInCart : productOrders.getProducts()) {
                                if (productInCart.getTitle().toLowerCase().contains(keySearch.toLowerCase())) {
                                    listOrders.add(productOrders);
                                    break;
                                }
                            }
                        }
                        ordersCallBack.onResult(listOrders);
                    } else {
                        ordersCallBack.onResult(null);
                    }
                })
                .addOnFailureListener(error -> ordersCallBack.onResult(null));
    }
}
