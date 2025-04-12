package com.example.techstore.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techstore.R;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.LoginViewModel;
import com.example.techstore.viewmodel.SignUpViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpImgActivity extends AppCompatActivity {

    SignUpViewModel signUpViewModel;
    UserRepository userRepository;
    ImageView signupIvImg;
    TextView signupTvUsername;
    Button btnComp;
    String encodeImg;

    private final ActivityResultLauncher<Intent>activityResultLauncher =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            signupIvImg.setImageBitmap(bitmap);
                        } else {
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_default_user);
                            signupIvImg.setImageResource(R.drawable.background_default_user);
                        }
                        encodeImg = enCodeImage(bitmap);
                        signUpViewModel.selectImage(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_img);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRepository(this);
        signUpViewModel = new SignUpViewModel(userRepository);

        signupIvImg = findViewById(R.id.signup_next_img);
        signupTvUsername = findViewById(R.id.signup_next_username);
        btnComp = findViewById(R.id.signup_btn_signup_comp);

        signupIvImg.setOnClickListener(selectImg -> {
            Intent choose = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            choose.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activityResultLauncher.launch(choose);
        });


        btnComp.setOnClickListener(comp -> {
            String username = signupTvUsername.getText().toString().trim();
            if (!encodeImg.isEmpty() && !username.isEmpty()) {
                Bundle bundle = getIntent().getBundleExtra(Constants.KEY_COLLECTION_USER);
                Map<String, String> user = new HashMap<>();
                user.put(Constants.KEY_USERNAME, username);
                user.put(Constants.KEY_EMAIL, bundle.getString(Constants.KEY_EMAIL));
                user.put(Constants.KEY_PASSWORD, bundle.getString(Constants.KEY_PASSWORD));
                user.put(Constants.KEY_PHONE, bundle.getString(Constants.KEY_PHONE));
                user.put(Constants.KEY_IMG, encodeImg);
                signUpViewModel.signup(user);
            } else {
                Toast.makeText(SignUpImgActivity.this, R.string.signup_error_input_inf, Toast.LENGTH_SHORT).show();
            }
        });

        signUpViewModel.getIsSignup().observe(this, isSignup -> {
            if (isSignup) {
                Intent intent = new Intent(SignUpImgActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        signUpViewModel.getMessageSignup().observe(this, message -> {
            Toast.makeText(SignUpImgActivity.this, message, Toast.LENGTH_SHORT).show();
        });
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