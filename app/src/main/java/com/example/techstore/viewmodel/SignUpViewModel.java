package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.User;
import com.example.techstore.repository.UserRepository;

import java.util.Map;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<String> messageSignup = new MutableLiveData<>("");
    MutableLiveData<Boolean> isSignup = new MutableLiveData<>(false);
    MutableLiveData<Boolean> checkEmail = new MutableLiveData<>(false);
    UserRepository userRepository;

    public SignUpViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<Boolean> getIsSignup() {
        return isSignup;
    }

    public MutableLiveData<String> getMessageSignup() {
        return messageSignup;
    }

    public MutableLiveData<Boolean> getCheckEmail() {
        return checkEmail;
    }

    public void checkEmailExis(String email) {
        userRepository.checkEmailisExits(email, result -> {
            if (result) checkEmail.setValue(true);
            else checkEmail.setValue(false);
        });
    }

    public void signup(Map<String, String> user ) {
        userRepository.signupSuccess(user, result -> {
            if (result) {
                isSignup.setValue(true);
                messageSignup.setValue("Đăng kí thành công");
            } else {
                isSignup.setValue(false);
                messageSignup.setValue("Kiểm tra lại thông tin");
            }
        });
    }
}
