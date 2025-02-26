package com.example.techstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techstore.MainActivity;
import com.example.techstore.R;
import com.example.techstore.untilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    LinearLayout login_layout_signup;
    TextInputEditText login_et_email, login_et_password;
    MaterialCheckBox login_cb_saveInf;
    MaterialTextView login_tv_forgotPassword;
    MaterialButton login_btn_login;
    Boolean flagEmail = true, flagPassword = true, isSaveInf = false;
    FirebaseFirestore firestore;
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

        sharedPreferences = getSharedPreferences(Constants.KEY_SHARE_PREFERENCE, MODE_PRIVATE);
        firestore = FirebaseFirestore.getInstance();
        login_layout_signup = findViewById(R.id.login_layout_signup);
        login_et_email = findViewById(R.id.login_et_email);
        login_et_password = findViewById(R.id.login_et_password);
        login_cb_saveInf = findViewById(R.id.login_cb_saveInf);
        login_tv_forgotPassword = findViewById(R.id.login_tv_forgotPassword);
        login_btn_login = findViewById(R.id.login_btn_login);

        Boolean isLogin = sharedPreferences.getBoolean(Constants.KEY_IS_LOGIN, false);
        if (!isLogin) {
            Boolean checkSaveInf = sharedPreferences.getBoolean(Constants.KEY_IS_SAVE_INF, false);
            if (checkSaveInf){
                String email = sharedPreferences.getString(Constants.KEY_EMAIL, "");
                String password = sharedPreferences.getString(Constants.KEY_PASSWORD, "");
                if (!email.isEmpty()) login_et_email.setText(email);
                if (!password.isEmpty()) login_et_password.setText(password);
            }
        }
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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

        login_btn_login.setOnClickListener(loginAct -> {
            login();
        });
    }

    public Boolean validLogin() {
        if (flagEmail && flagPassword) return true;
        else return false;
    }

    public void login() {
        String email = login_et_email.getText().toString();
        String password = login_et_password.getText().toString();
        isSaveInf = login_cb_saveInf.isChecked();
        if (validLogin()) {
            firestore.collection(Constants.KEY_COLLECTION_USER)
                    .whereEqualTo(Constants.KEY_EMAIL, email)
                    .whereEqualTo(Constants.KEY_PASSWORD, password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && !task.getResult().isEmpty()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.KEY_EMAIL, email);
                                editor.putString(Constants.KEY_PASSWORD, password);
                                editor.putBoolean(Constants.KEY_IS_LOGIN, true);
                                editor.putBoolean(Constants.KEY_IS_SAVE_INF, isSaveInf);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, R.string.login_falure, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, R.string.login_falure, Toast.LENGTH_SHORT).show();
        }
    }
}