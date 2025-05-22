package com.example.techstore.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    ProductRepository productRepository;
    MutableLiveData<List<Product>> favoriteProducts = new MutableLiveData<>();
    @SuppressLint("StaticFieldLeak")
    Context context;

    public FavoriteViewModel(Context context, ProductRepository productRepository) {
        this.context = context;
        this.productRepository = productRepository;
    }

    public MutableLiveData<List<Product>> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void loadFavoriteProduct() {
        productRepository.getFavoriteProduct(result -> {
            favoriteProducts.setValue(result);
        });
    }
}
