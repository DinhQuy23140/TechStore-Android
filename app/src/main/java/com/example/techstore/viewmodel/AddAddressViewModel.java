package com.example.techstore.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.R;
import com.example.techstore.model.Address;
import com.example.techstore.model.Province;
import com.example.techstore.repository.AddressRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.sharepreference.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class AddAddressViewModel extends ViewModel {
    MutableLiveData<List<Province>> listProvince = new MutableLiveData<>();
    MutableLiveData<List<Address>> listAddress = new MutableLiveData<>();
    MutableLiveData<Address> address = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);
    MutableLiveData<String> message = new MutableLiveData<>("");
    AddressRepository addressRepository;
    SharedPrefManager sharedPrefManager;
    Context context;

    public AddAddressViewModel(Context context, AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);
    }


    public MutableLiveData<List<Province>> getListProvince() {
        return listProvince;
    }

    public MutableLiveData<List<Address>> getListAddress() {
        return listAddress;
    }

    public MutableLiveData<Address> getAddress() {
        return address;
    }



    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void loadAddress() {
        addressRepository.loadAdress();
        listProvince = addressRepository.getListAddress();
    }

    public void addAddress(String nameProvince, String nameDistrict, String nameWard, String detail, String type, boolean isDefault) {
        String name = sharedPrefManager.getUserName();
        String phone = sharedPrefManager.getPhone();
        Address address = new Address(detail, nameDistrict, name, phone, nameProvince, type, nameWard, isDefault);
        addressRepository.addAddress(address, result -> {
            if (result) {
                message.setValue(context.getString(R.string.address_add_success));
                isSuccess.setValue(true);
            }
            else {
                message.setValue(context.getString(R.string.address_add_failure));
                isSuccess.setValue(false);
            }
        });
    }

    public void getAllAddress() {
        addressRepository.getAddress(result -> {
            if (result != null && !result.isEmpty()) {
                listAddress.setValue(result);
            } else {
                listAddress.setValue(new ArrayList<>());
            }
        });
    }

    public void getDefaultAddress() {
        addressRepository.getDefaultAddress(result -> {
            if (result != null) {
                address.setValue(result);
            } else {
                address.setValue(null);
            }
        });
    }

}
