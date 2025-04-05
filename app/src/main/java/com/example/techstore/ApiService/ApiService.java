package com.example.techstore.ApiService;

import com.example.techstore.model.product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products")
    Call<ArrayList<product>> getProduct();
}
