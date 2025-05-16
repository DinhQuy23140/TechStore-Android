package com.example.techstore.viewmodel;

import android.graphics.Bitmap;
import android.util.Base64;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.techstore.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<String> messageSignup = new MutableLiveData<>("");
    MutableLiveData<Boolean> isSignup = new MutableLiveData<>(false);
    MutableLiveData<Boolean> checkEmail = new MutableLiveData<>(false);
    MutableLiveData<Bitmap> imgBitMap = new MutableLiveData<>();
    MutableLiveData<String> strImg = new MutableLiveData<>("");
    UserRepository userRepository;

    public SignUpViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<Boolean> getCheckEmail() {
        return checkEmail;
    }

    public MutableLiveData<String> getMessageSignup() {
        return messageSignup;
    }

    public MutableLiveData<Boolean> getIsSignup() {
        return isSignup;
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

    public void signupTest(Map<String, String> user ) {
        userRepository.signupTest(user, result -> {
            if (result) {
                isSignup.setValue(true);
                messageSignup.setValue("Đăng kí thành công");
            } else {
                isSignup.setValue(false);
                messageSignup.setValue("Kiểm tra lại thông tin");
            }
        });
    }

    public void selectImage (Bitmap bitmap) {
        String imgStr = enCodeImage(bitmap);
        strImg.setValue(imgStr);
    }

    private String enCodeImage(Bitmap bitmap){
        //set with
        int previewWith = 150;
        //set height
        int previewHeight = bitmap.getHeight() * previewWith / bitmap.getWidth();
        //scale image
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
