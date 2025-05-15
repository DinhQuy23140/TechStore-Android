package com.example.techstore.ApiService;

import com.example.techstore.model.Product;
import com.example.techstore.model.Province;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("products")
    Call<ArrayList<Product>> getProduct();

    @GET("api/")
    Call<List<Province>> getProvince(@Query("depth") int depth);
}
