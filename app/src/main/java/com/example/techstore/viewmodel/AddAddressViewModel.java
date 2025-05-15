package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.Province;
import com.example.techstore.repository.AddressRepository;

import java.util.List;

public class AddAddressViewModel extends ViewModel {
    MutableLiveData<List<Province>> listAddress = new MutableLiveData<>();
    AddressRepository addressRepository;

    public AddAddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public MutableLiveData<List<Province>> getListAddress() {
        return listAddress;
    }

    public void loadAddress() {
        addressRepository.loadAdress();
        listAddress = addressRepository.getListAddress();
    }
}
