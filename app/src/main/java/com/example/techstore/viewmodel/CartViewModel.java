package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.ProductInCart;
import com.example.techstore.repository.UserRepository;

import java.util.Map;

public class CartViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<String> message = new MutableLiveData<>("");

    public CartViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void addCart(ProductInCart product) {
        userRepository.addProduct(product, result -> {
            if (result) message.setValue("Thêm sản phẩm thành công vào giỏ hàng");
            else message.setValue("Có lỗi xảy ra!");
        });
    }
}
