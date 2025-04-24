package com.example.techstore.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;

import java.util.List;

public class OrdersViewModel extends ViewModel {

    MutableLiveData<String> message = new MediatorLiveData<>("");
    MutableLiveData<Boolean> isSuccess = new MediatorLiveData<>(false);
    MutableLiveData<List<String>> listOrders = new MediatorLiveData<>();
    OrdersRepository ordersRepository;

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<List<String>> getListOrders() {
        return listOrders;
    }

    public OrdersViewModel(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void addOrders(ProductOrders productOrders){
        ordersRepository.addOrders(productOrders, result -> {
            if (result) {
                isSuccess.setValue(true);
                message.setValue("Thanh cong");
            } else {
                isSuccess.setValue(false);
                message.setValue("That bai");
            }
        });
    }

    public void getOrders() {
        ordersRepository.getOrders(result -> {
            if (!result.isEmpty()) {
                listOrders.setValue(result);
            }
        });
    }
}
