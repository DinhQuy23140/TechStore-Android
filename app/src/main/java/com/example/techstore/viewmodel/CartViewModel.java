package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.ProductInCart;
import com.example.techstore.repository.UserRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<String> message = new MutableLiveData<>("");
    MutableLiveData<List<ProductInCart>> listProduct = new MutableLiveData<>();

    public CartViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<List<ProductInCart>> getListProduct() {
        return listProduct;
    }

    public void setListProduct(MutableLiveData<List<ProductInCart>> listProduct) {
        this.listProduct = listProduct;
    }

    public void addOrUpdateCart(ProductInCart product) {
        userRepository.addOrUpdateProduct(product, result -> {
            if (result) message.setValue("Thêm sản phẩm thành công vào giỏ hàng");
            else message.setValue("Có lỗi xảy ra!");
        });
    }

    public void deleteProduct(ProductInCart product) {
        userRepository.deleteProductInCart(product);
    }

    public void updateQuantity(ProductInCart product) {
        userRepository.updateQuantityProduct(product);
    }

    public void getCart() {
        userRepository.getProductInCart(result -> {
            if (result != null &&!result.isEmpty()) {
                Gson gson = new Gson();
                List<ProductInCart> list = new ArrayList<>();
                for (String strProduct : result) {
                    ProductInCart productInCart = gson.fromJson(strProduct, ProductInCart.class);
                    list.add(productInCart);
                }
                listProduct.setValue(list);
            } else {
                listProduct.setValue(new ArrayList<>());
            }
        });
    }

    public void clearCart(List<ProductInCart> listProduct) {
        userRepository.clearCart(listProduct);
    }

}
