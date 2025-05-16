package com.example.techstore.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.AddressClient;
import com.example.techstore.model.Address;
import com.example.techstore.model.Province;
import com.example.techstore.sharepreference.SharedPrefManager;
import com.example.techstore.untilities.Constants;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    MutableLiveData<List<Province>> listAddress = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);
    MutableLiveData<String> message = new MutableLiveData<>("");
    FirebaseFirestore firestore;
    SharedPrefManager sharedPrefManager;
    Context context;
    Gson gson;

    public interface CallBack {
        public void onResult(boolean result);
    }

    public interface CallBackList {
        public void onListResult(List<Address> result);
    }
    public AddressRepository(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        sharedPrefManager = new SharedPrefManager(context);
        gson = new Gson();
    }

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

    public void addAddress(Address address, CallBack callBack) {
        String email = sharedPrefManager.getEmail();
        String strAddress = gson.toJson(address);
        firestore.collection(Constants.KEY_COLLECTION_USER)
                .document(email)
                .update(Constants.KEY_ADDRESS, FieldValue.arrayUnion(strAddress))
                .addOnSuccessListener(documentReference -> callBack.onResult(true))
                .addOnFailureListener(e -> callBack.onResult(false));
    }

    public void getAddress(CallBackList callback) {
        String email = sharedPrefManager.getEmail();
        firestore.collection(Constants.KEY_COLLECTION_USER)
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> listAddress = (List<String>) documentSnapshot.get(Constants.KEY_ADDRESS);
                    List<Address> result = new ArrayList<>();
                    if (listAddress != null) {
                        for (String strAddress : listAddress) {
                            Address address = gson.fromJson(strAddress, Address.class);
                            result.add(address);
                        }
                        callback.onListResult(result);
                    } else {
                        callback.onListResult(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> callback.onListResult(new ArrayList<>()));
    }
}
