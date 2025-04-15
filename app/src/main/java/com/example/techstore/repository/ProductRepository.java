package com.example.techstore.repository;

import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.RetrofitClient;
import com.example.techstore.model.Product;
import com.example.techstore.untilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductRepository {
    FirebaseFirestore firebaseFirestore;

    public ProductRepository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public interface Callback {
        void onResult(List<Product> result);
    }

    public void getProduct(Callback callback) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ArrayList<Product>> call = apiService.getProduct();
        call.enqueue(new retrofit2.Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    callback.onResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable throwable) {

            }
        });
    }

}
