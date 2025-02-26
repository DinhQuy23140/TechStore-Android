package com.example.techstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techstore.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {

    LinearLayout login_layout_signup;
    TextInputEditText login_et_email, login_et_password;
    MaterialCheckBox login_cb_saveInf;
    MaterialTextView login_tv_forgotPassword;
    MaterialButton login_btn_login;
    Boolean flagEmail = true, flagPassword = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        login_layout_signup = findViewById(R.id.login_layout_signup);
        login_et_email = findViewById(R.id.login_et_email);
        login_et_password = findViewById(R.id.login_et_password);
        login_cb_saveInf = findViewById(R.id.login_cb_saveInf);
        login_tv_forgotPassword = findViewById(R.id.login_tv_forgotPassword);
        login_btn_login = findViewById(R.id.login_btn_login);

        login_layout_signup.setOnClickListener(viewSignUp -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        login_et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = login_et_email.getText().toString();
                if(!hasFocus) {
                    if (email.isEmpty()) {
                        login_et_email.setError(getString(R.string.login_error_email));
                        flagEmail = false;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        login_et_email.setError(getString(R.string.login_error_email_format));
                        flagEmail = false;
                    } else {
                        login_et_email.setError(null);
                        flagEmail = true;
                    }
                }
            }
        });

        login_et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String password = login_et_password.getText().toString();
                if(!hasFocus) {
                    if(password.isEmpty()) {
                        login_et_password.setError(getString(R.string.login_error_password));
                        flagPassword = false;
                    } else if (password.length() < 8) {
                        login_et_password.setError(getString(R.string.login_error_password_length ));
                        flagPassword = false;
                    } else {
                        login_et_password.setError(null);
                        flagPassword = true;
                    }
                }
            }
        });
    }
}