package com.example.techstore.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    ProductRepository productRepository;
    UserRepository userRepository;
    MutableLiveData<List<Product>> favoriteProducts = new MutableLiveData<>();
    @SuppressLint("StaticFieldLeak")
    Context context;

    public FavoriteViewModel(Context context, ProductRepository productRepository, UserRepository userRepository) {
        this.context = context;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<Product>> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void loadFavoriteProduct() {
        productRepository.getFavoriteProduct(result -> {
            favoriteProducts.setValue(result);
        });
    }

    public void addFavoriteProduct(Product product) {
        userRepository.addFavoriteProduct(product);
    }

    public void unFavoriteProduct(Product product) {
        userRepository.unFavoriteProduct(product);
    }
}
