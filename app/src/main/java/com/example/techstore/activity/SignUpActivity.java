package com.example.techstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techstore.R;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.SignUpViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout signup_layout_login;
    TextInputEditText signup_et_email, signup_et_password, signup_et_confirm_password, signup_et_phone;
    MaterialButton signup_btn_signup;
    Boolean flagEmail = true, flagPassword = true, flagConfirmPassword = true, flagPhone = true;
    FirebaseFirestore firestore;
    SignUpViewModel signupViewModel;
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);
        signupViewModel = new SignUpViewModel(userRepository);
        firestore = FirebaseFirestore.getInstance();
        signup_layout_login = findViewById(R.id.signup_layout_login);
        signup_et_email = findViewById(R.id.signup_et_email);
        signup_et_password = findViewById(R.id.signup_et_password);
        signup_et_confirm_password = findViewById(R.id.signup_et_confirm_password);
        signup_et_phone = findViewById(R.id.signup_et_phone);
        signup_btn_signup = findViewById(R.id.signup_btn_signup);

        signup_layout_login.setOnClickListener(viewLogin -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signup_et_email.setOnFocusChangeListener((v, hasFocus) -> {
            String email = Objects.requireNonNull(signup_et_email.getText()).toString();
            if(!hasFocus) {
                if(email.isEmpty()) {
                    signup_et_email.setError(getString(R.string.login_error_email));
                    flagEmail = false;
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signup_et_email.setError(getString(R.string.login_error_email_format));
                    flagEmail = false;
                    return;
                }
                signupViewModel.checkEmailExis(email);
            }
        });

        signupViewModel.getCheckEmail().observe(this, checkEmail -> {
            if (checkEmail) {
                flagEmail = false;
                signup_et_email.setError("Email already exists");
            } else {
                flagEmail = true;
            }
        });

        signup_et_password.setOnFocusChangeListener((v, hasFocus) -> {
            String password = Objects.requireNonNull(signup_et_password.getText()).toString();
            if(!hasFocus) {
                if(password.isEmpty()) {
                    signup_et_password.setError(getString(R.string.login_error_password));
                    flagPassword = false;
                } else if (password.length() < 8) {
                    signup_et_password.setError(getString(R.string.login_error_password_length ));
                    flagPassword = false;
                } else {
                    signup_et_password.setError(null);
                    flagPassword = true;
                }
            }
        });

        signup_et_confirm_password.setOnFocusChangeListener((v, hasFocus) -> {
            String confirmPassword = Objects.requireNonNull(signup_et_confirm_password.getText()).toString();
            String password = Objects.requireNonNull(signup_et_password.getText()).toString();
            if(!hasFocus) {
                if(confirmPassword.isEmpty()) {
                    signup_et_confirm_password.setError(getString(R.string.login_error_password));
                    flagConfirmPassword = false;
                } else if (!confirmPassword.equals(password)) {
                    signup_et_confirm_password.setError(getString(R.string.signup_error_password_not_match));
                    flagConfirmPassword = false;
                } else {
                    signup_et_confirm_password.setError(null);
                    flagConfirmPassword = true;
                }
            }
        });

        signup_et_phone.setOnFocusChangeListener((v, hasFocus) -> {
            String phone = Objects.requireNonNull(signup_et_phone.getText()).toString();
            if(!hasFocus) {
                if (phone.isEmpty()) {
                    signup_et_phone.setError(getString(R.string.signup_error_phone));
                    flagPhone = false;
                } else if (phone.length() < 10) {
                    signup_et_phone.setError(getString(R.string.signup_error_phone_format));
                    flagPhone = false;
                } else {
                    signup_et_phone.setError(null);
                    flagPhone = true;
                }
            }
        });

        signup_btn_signup.setOnClickListener(signup_act -> signup());
//        signupViewModel.getIsSignup().observe(this, isSignup -> {
//            if (isSignup) {
//                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });
//
//        signupViewModel.getMessageSignup().observe(this, message -> Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    public Boolean validation() {
        return flagEmail && flagPassword && flagConfirmPassword && flagPhone;
    }

    public void signup() {
        if (validation()) {
            String email = Objects.requireNonNull(signup_et_email.getText()).toString();
            String password = Objects.requireNonNull(signup_et_password.getText()).toString();
            String phone = Objects.requireNonNull(signup_et_phone.getText()).toString();
            Intent intent = new Intent(SignUpActivity.this, SignUpImgActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_EMAIL, email);
            bundle.putString(Constants.KEY_PASSWORD, password);
            bundle.putString(Constants.KEY_PHONE, phone);
            intent.putExtra(Constants.KEY_COLLECTION_USER, bundle);
            startActivity(intent);
//            Map<String, String> newUser = new HashMap<>();
//            newUser.put(Constants.KEY_EMAIL, email);
//            newUser.put(Constants.KEY_PASSWORD, password);
//            newUser.put(Constants.KEY_PHONE, phone);
//            signupViewModel.signup(newUser);
        }
        else {
            Toast.makeText(SignUpActivity.this, R.string.signup_error_input_inf, Toast.LENGTH_SHORT).show();
        }
    }
}