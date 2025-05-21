package com.example.techstore.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;

public class FavoriteViewModel extends ViewModel {
    ProductRepository productRepository;
    MutableLiveData<Product> favoriteProducts = new MediatorLiveData<>();
    @SuppressLint("StaticFieldLeak")
    Context context;

    public FavoriteViewModel(Context context, ProductRepository productRepository) {
        this.context = context;
        this.productRepository = productRepository;
    }

    public void loadFavoriteProduct() {
        productRepository.();
    }
}
