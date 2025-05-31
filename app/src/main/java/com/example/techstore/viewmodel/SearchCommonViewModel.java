package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchCommonViewModel extends ViewModel {
    MutableLiveData<List<ProductInCart>> listProductInCart = new MutableLiveData<>();
    MutableLiveData<List<ProductOrders>> listOrders = new MutableLiveData<>();
    MutableLiveData<List<ProductOrders>> listOrdersComplete = new MutableLiveData<>();
    MutableLiveData<List<ProductOrders>> listOrdersCancel = new MutableLiveData<>();
    UserRepository userRepository;
    OrdersRepository ordersRepository;

    public SearchCommonViewModel(UserRepository userRepository, OrdersRepository ordersRepository) {
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
    }

    public MutableLiveData<List<ProductOrders>> getListOrders() {
        return listOrders;
    }

    public MutableLiveData<List<ProductOrders>> getListOrdersCancel() {
        return listOrdersCancel;
    }

    public MutableLiveData<List<ProductOrders>> getListOrdersComplete() {
        return listOrdersComplete;
    }

    public MutableLiveData<List<ProductInCart>> getListProductInCart() {
        return listProductInCart;
    }

    public void getProductInCartByTitle(String keySearch) {
        userRepository.seachProductInCart(keySearch, cart -> {
            if (cart != null) {
                listProductInCart.setValue(cart);
            } else listProductInCart.setValue(new ArrayList<>());
        });
    }

    public void getOrdersByTitle(String keySearch) {
        ordersRepository.getOrdersByTitle(keySearch, orders -> {
            if (orders != null && !orders.isEmpty()) {
                listOrders.setValue(orders);
            } else listOrders.setValue(new ArrayList<>());
        });
    }

    public void getOrdersCompleteByTitle(String keySearch) {
        ordersRepository.getOrdersCompleteByTitle(keySearch, completed -> {
            if (completed != null && !completed.isEmpty()) {
                listOrdersComplete.setValue(completed);
            } else listOrdersComplete.setValue(new ArrayList<>());
        });
    }

    public void getOrdersCancelByTitle(String keySearch) {
        ordersRepository.getOrdersCancelByTitle(keySearch, cancel -> {
            if (cancel != null && !cancel.isEmpty()) {
                listOrdersCancel.setValue(cancel);
            } else listOrdersCancel.setValue(new ArrayList<>());
        });
    }


}
