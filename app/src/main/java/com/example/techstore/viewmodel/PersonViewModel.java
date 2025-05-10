package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PersonViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<String> imgUser = new MutableLiveData<>("");
    MutableLiveData<String> username = new MutableLiveData<>("");
    MutableLiveData<String> email = new MutableLiveData<>("");
    MutableLiveData<String> phone = new MutableLiveData<>("");
    MutableLiveData<String> dob = new MutableLiveData<>("");
    MutableLiveData<String> message = new MutableLiveData<>("");
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

    public MutableLiveData<String> getDob() {
        return dob;
    }

    public void setDob(MutableLiveData<String> dob) {
        this.dob = dob;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void setMessage(MutableLiveData<String> message) {
        this.message = message;
    }

    public void loadUser() {
        imgUser.setValue(userRepository.getImg());
        username.setValue(userRepository.getUserName());
        email.setValue(userRepository.getEmail());
        phone.setValue(userRepository.getPhone());
        dob.setValue(userRepository.getDoB());
    }

    public void updateUser(Map<String, Object> user) {
        userRepository.updateUser(user, result -> {
            if (result) {
                loadUser();
                message.setValue("Cập nhập thành công");
            } else {
                message.setValue("Có lỗi xảy ra!");
            }
        });
    }
}
