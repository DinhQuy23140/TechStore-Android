package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;

import java.util.ArrayList;

public class HomeViewModel {
    ProductRepository productRepository;
    MutableLiveData<ArrayList<Product>> listProduct = new MutableLiveData<>();

    public HomeViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MutableLiveData<ArrayList<Product>> getListProduct() {
        return listProduct;
    }

    public void setListProduct(MutableLiveData<ArrayList<Product>> listProduct) {
        this.listProduct = listProduct;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void getProduct() {
        productRepository.getProduct(products -> {
            if (!products.isEmpty()) {
                listProduct.setValue((ArrayList<Product>) products);
            }
        });
    }
}
