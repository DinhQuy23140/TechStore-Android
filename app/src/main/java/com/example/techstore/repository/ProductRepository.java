package com.example.techstore.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.ProductClient;
import com.example.techstore.model.Product;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class ProductRepository {
    FirebaseFirestore firebaseFirestore;
    SharedPrefManager sharedPrefManager;
    MutableLiveData<List<Product>> listProduct = new MutableLiveData<>();
    Gson gson;

    public ProductRepository(Context context) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPrefManager = new SharedPrefManager(context);
        gson = new Gson();
    }

    public interface Callback {
        void onResult(List<Product> result);
    }

    public LiveData<List<Product>> getListProduct() { return listProduct; } // List<Product>>

    public void loadProduct() {
//        ApiService apiService = ProductClient.getInstance().create(ApiService.class);
//        Call<ArrayList<Product>> call = apiService.getProduct();
//
//        call.enqueue(new retrofit2.Callback<ArrayList<Product>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
//                if (response.isSuccessful() && !response.body().isEmpty()) {
//                    List<Product> result = response.body();
//                    listenToFavoriteChange(result);
//                } else {
//                    listProduct.setValue(new ArrayList<>());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Product>> call, Throwable throwable) {
//                listProduct.setValue(new ArrayList<>());
//            }
//        });
        Gson gson = new Gson();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_PRODUCT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        List<Product> result = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            String json = gson.toJson(data);
                            Product product = gson.fromJson(json, Product.class);
                            result.add(product);
                        }
                        listenToFavoriteChange(result);
                    }
                });
    }

    private void listenToFavoriteChange(List<Product> apiPorducts) {
        firebaseFirestore.collection(Constants.KEY_COLLECTION_FAVORITE)
                .document(sharedPrefManager.getEmail())
                .collection(Constants.KEY_COLLECTION_PRODUCT)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null || snapshot == null) {
                        listProduct.setValue(apiPorducts);
                        return;
                    }

                    Set<String> favoriteIds = new HashSet<>();
                    for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()) {
                        favoriteIds.add(documentSnapshot.getId());
                    }

                    for (Product product : apiPorducts) {
                        product.setFavorite(favoriteIds.contains(String.valueOf(product.getId())));
                    }

                    listProduct.setValue(apiPorducts);
                });
    }

    public void getFavoriteProduct(Callback callback) {
        String email = sharedPrefManager.getEmail();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_FAVORITE)
                .document(email)
                .collection(Constants.KEY_COLLECTION_PRODUCT)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<Product> result = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        String strProduct = documentSnapshot.getString(Constants.KEY_VALUE);
                        Product product = gson.fromJson(strProduct, Product.class);
                        result.add(product);
                    }
                    callback.onResult(result);
                })
                .addOnFailureListener(e -> callback.onResult(new ArrayList<>()));
    }

    public void getProductByTitle(String search, Callback callback) {
        ApiService apiService = ProductClient.getInstance().create(ApiService.class);
        Call<ArrayList<Product>> call = apiService.getProduct();
        call.enqueue(new retrofit2.Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Product> result = new ArrayList<>();
                    if (!response.body().isEmpty()) {
                        for (Product product : response.body()) {
                            String  keyTitlte = product.getTitle().toLowerCase();
                            if (keyTitlte.contains(search)) {
                                result.add(product);
                            }
                        }
                        callback.onResult(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable throwable) {
                callback.onResult(new ArrayList<>());
            }
        });
    }
}
