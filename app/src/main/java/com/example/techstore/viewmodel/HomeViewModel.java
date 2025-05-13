package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeViewModel {
    ProductRepository productRepository;
    UserRepository userRepository;
    MutableLiveData<ArrayList<Product>> listProduct = new MutableLiveData<>();
    MutableLiveData<List<Integer>> listPathImageCategory = new MutableLiveData<>();
    MutableLiveData<List<String>> listNameCategory = new MutableLiveData<>();
    MutableLiveData<List<Integer>> listImgViewPage = new MutableLiveData<>();
    MutableLiveData<String> username = new MutableLiveData<>("");
    MutableLiveData<String> img = new MutableLiveData<>("");
    MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);

    public MutableLiveData<String> getImg() {
        return img;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public HomeViewModel(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public MutableLiveData<ArrayList<Product>> getListProduct() {
        return listProduct;
    }

    public void getProduct() {
        productRepository.getProduct(products -> {
            if (!products.isEmpty()) {
                listProduct.setValue((ArrayList<Product>) products);
            }
        });
    }

    public void loadCategories() {
        List<Integer> listImg = Arrays.asList(R.drawable.baseline_border_all_34, R.drawable.baseline_battery_charging_full_24, R.drawable.baseline_camera_alt_24, R.drawable.baseline_headphones_24,
                R.drawable.baseline_keyboard_24, R.drawable.baseline_laptop_24, R.drawable.baseline_mouse_24, R.drawable.baseline_router_24, R.drawable.baseline_sd_storage_24,
                R.drawable.baseline_smartphone_24, R.drawable.baseline_speaker_24, R.drawable.baseline_tablet_24, R.drawable.baseline_watch_24);
        List<String> listName = Arrays.asList("All", "Battery change", "Camera", "Headphone", "Keyboard", "Laptop", "Mouse",
                "Router", "Storage", "Smartphone", "Speaker", "Tablet", "Watch");
        listPathImageCategory.setValue(listImg);
        listNameCategory.setValue(listName);
    }

    public void loadImgViewPage() {
        List<Integer> listPathImage = new ArrayList<>();
        listPathImage = new ArrayList<>();
        listPathImage.add(R.drawable.img);
        listPathImage.add(R.drawable.img_1);
        listPathImage.add(R.drawable.img_2);
        listPathImage.add(R.drawable.img_3);
        listImgViewPage.setValue(listPathImage);
    }

    public MutableLiveData<List<Integer>> getListImgViewPage() {
        return listImgViewPage;
    }

    public MutableLiveData<List<String>> getListNameCategory() {
        return listNameCategory;
    }

    public MutableLiveData<List<Integer>> getListPathImageCategory() {
        return listPathImageCategory;
    }

    public void loadUser() {
        username.setValue(userRepository.getUserName());
        img.setValue(userRepository.getImg());
    }

    public void addFavoriteProduct(Product product){
        userRepository.addFavoriteProduct(product);
    }
}
