package com.example.techstore.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.AddressClient;
import com.example.techstore.model.Province;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    MutableLiveData<List<Province>> listAddress = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);
    MutableLiveData<String> message = new MutableLiveData<>("");

    public MutableLiveData<List<Province>> getListAddress() {
        return listAddress;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void loadAdress() {
        ApiService apiService = AddressClient.getClient().create(ApiService.class);
        Call<List<Province>> call = apiService.getProvince(3);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful()) {
                    listAddress.setValue(response.body());
                    Log.d("success", response.body().toString());
                    isSuccess.setValue(true);
                } else {
                    listAddress.setValue(new ArrayList<>());
                    isSuccess.setValue(false);
                    message.setValue(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable throwable) {
                listAddress.setValue(new ArrayList<>());
                isSuccess.setValue(false);
                message.setValue(throwable.getMessage());
            }
        });
    }
}
