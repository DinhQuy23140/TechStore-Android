package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    UserRepository userRepository;
    ProductRepository productRepository;
    MutableLiveData<List<String>> search = new MutableLiveData<>();
    MutableLiveData<List<Product>> listProduct = new MutableLiveData<>();

    public SearchViewModel(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public MutableLiveData<List<String>> getSearch() {
        return search;
    }

    public MutableLiveData<List<Product>> getListProduct() {
        return listProduct;
    }

    public void addSearch(String search) {
        userRepository.addSearch(search);
    }

    public void getHistory() {
        userRepository.getSearch(result -> {
            search.setValue(result);
        });
    }

    public void deleteHistory(String search) {
        userRepository.deleteSearch(search);
    }

    public void getProductByTitle(String title) {
        productRepository.getProductByTitle(title, result -> {
            listProduct.setValue(result);
        });
    }
}
