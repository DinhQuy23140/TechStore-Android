package com.example.techstore.repository;
import android.content.Context;

import com.example.techstore.model.ProductOrders;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.local.OverlayedDocument;
import com.google.gson.Gson;

import java.lang.reflect.Field;
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
}
