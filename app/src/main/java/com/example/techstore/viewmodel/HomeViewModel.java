package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeViewModel {
    ProductRepository productRepository;
    MutableLiveData<ArrayList<Product>> listProduct = new MutableLiveData<>();
    MutableLiveData<List<Integer>> listPathImageCategory = new MutableLiveData<>();
    MutableLiveData<List<String>> listNameCategory = new MutableLiveData<>();

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

    public void loadCategories() {
        List<Integer> listImg = Arrays.asList(R.drawable.baseline_battery_charging_full_24, R.drawable.baseline_camera_alt_24, R.drawable.baseline_headphones_24,
                R.drawable.baseline_keyboard_24, R.drawable.baseline_laptop_24, R.drawable.baseline_mouse_24, R.drawable.baseline_router_24, R.drawable.baseline_sd_storage_24,
                R.drawable.baseline_smartphone_24, R.drawable.baseline_speaker_24, R.drawable.baseline_tablet_24, R.drawable.baseline_watch_24);
        List<String> listName = Arrays.asList("Battery change", "Camera", "Headphone", "Keyboard", "Laptop", "Mouse",
                "Router", "Storage", "Smartphone", "Speaker", "Tablet", "Watch");
        listPathImageCategory.setValue(listImg);
        listNameCategory.setValue(listName);
    }

    public MutableLiveData<List<String>> getListNameCategory() {
        return listNameCategory;
    }

    public MutableLiveData<List<Integer>> getListPathImageCategory() {
        return listPathImageCategory;
    }
}
