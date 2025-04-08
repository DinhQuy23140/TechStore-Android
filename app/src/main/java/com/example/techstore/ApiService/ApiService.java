package com.example.techstore.ApiService;

import com.example.techstore.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products")
    Call<ArrayList<Product>> getProduct();
}
