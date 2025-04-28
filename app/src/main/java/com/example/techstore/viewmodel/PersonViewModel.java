package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<String> imgUser = new MutableLiveData<>("");
    MutableLiveData<String> username = new MutableLiveData<>("");
    MutableLiveData<String> email = new MutableLiveData<>("");
    MutableLiveData<String> phone = new MutableLiveData<>("");
    MutableLiveData<List<Integer>> listPath = new MutableLiveData<>();
    MutableLiveData<List<String>> listTitle = new MutableLiveData<>();

    public PersonViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<String> getImgUser() {
        return imgUser;
    }

    public void setImgUser(MutableLiveData<String> imgUser) {
        this.imgUser = imgUser;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public void setPhone(MutableLiveData<String> phone) {
        this.phone = phone;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public void setUsername(MutableLiveData<String> username) {
        this.username = username;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(MutableLiveData<String> email) {
        this.email = email;
    }

    public void loadUser() {
        imgUser.setValue(userRepository.getImg());
        username.setValue(userRepository.getUserName());
        email.setValue(userRepository.getEmail());
        phone.setValue(userRepository.getPhone());
    }

}
