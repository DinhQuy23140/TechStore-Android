package com.example.techstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techstore.model.User;
import com.example.techstore.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    UserRepository userRepository;
    MutableLiveData<Boolean> isLogin = new MutableLiveData<>(false);
    MutableLiveData<String> messageLogin = new MutableLiveData<>("");

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(MutableLiveData<Boolean> isLogin) {
        this.isLogin = isLogin;
    }

    public MutableLiveData<String> getMessageLogin() {
        return messageLogin;
    }

    public boolean isLogin() {
        return userRepository.getSharedPrefManager().isLogin();
    }

    public boolean isSaveInf() {
        return userRepository.getSharedPrefManager().isSaveInf();
    }

    public String getEmail() {
        return userRepository.getSharedPrefManager().getEmail();
    }

    public String getPassword() {
        return userRepository.getSharedPrefManager().getPassword();
    }

    public void login(User user, boolean isSaveInf) {
        if (!user.validEmail()) {
            messageLogin.setValue("Email không hợp lệ");
        } else if (!user.validPassword()) {
            messageLogin.setValue("Mật không khó hợp lệ");
        } else {
            userRepository.loginSuccess(user, isSaveInf, success -> {
                if (success) {
                    isLogin.setValue(true);
                    messageLogin.setValue("Đăng nhập thành công");
                } else {
                    isLogin.setValue(false);
                    messageLogin.setValue("Email hoặc mật khẩu không hợp lệ");
                }
            });
        }

    }
}
