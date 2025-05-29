package com.example.techstore.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;

import java.util.ArrayList;
import java.util.List;

public class OrdersViewModel extends ViewModel {

    MutableLiveData<String> message = new MediatorLiveData<>("");
    MutableLiveData<Boolean> isSuccess = new MediatorLiveData<>(false);
    MutableLiveData<List<String>> listOrders = new MediatorLiveData<>();
    MutableLiveData<List<String>> listCompleteOrders = new MediatorLiveData<>();
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

    public MutableLiveData<List<String>> getListCompleteOrders() {
        return listCompleteOrders;
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

    public void addCompleteOrders(ProductOrders productOrders){
        ordersRepository.addOrdersComplete(productOrders);
    }

    public void addCancelOrders(ProductOrders productOrders) {
        ordersRepository.addOrdersCancel(productOrders);
    }

    public void getOrders() {
        ordersRepository.getOrders(result -> {
            if (result != null && !result.isEmpty()) {
                listOrders.setValue(result);
            } else {
                listOrders.setValue(new ArrayList<>());
            }
        });
    }

    public void getCompleteOrders() {
        ordersRepository.getCompleteOrders(result -> {
            if (result != null && !result.isEmpty()) {
                listCompleteOrders.setValue(result);
            } else {
                listCompleteOrders.setValue(new ArrayList<>());
            }
        });
    }

    public void deleteOrders(ProductOrders productOrders){
        ordersRepository.deleteOrders(productOrders);
    }
}
