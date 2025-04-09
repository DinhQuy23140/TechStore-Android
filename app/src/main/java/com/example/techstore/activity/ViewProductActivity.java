package com.example.techstore.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.untilities.Constants;
import com.google.gson.Gson;

public class ViewProductActivity extends AppCompatActivity {

    ImageView btnBack, favorite, starRating;
    ImageView imageProduct;
    TextView nameProduct, rating, ratingCount, description, priceProduct;
    LinearLayout addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.productBack);
        imageProduct = findViewById(R.id.imgVP);
        favorite = findViewById(R.id.favoriteBtn);
        starRating = findViewById(R.id.starRating);
        nameProduct = findViewById(R.id.productName);
        rating = findViewById(R.id.ratingProduct);
        ratingCount = findViewById(R.id.ratingCount);
        description = findViewById(R.id.desContent);
        priceProduct = findViewById(R.id.priceProduct);
        addToCart = findViewById(R.id.addToCart);

        Intent intent = getIntent();
        String productStr = intent.getStringExtra(Constants.KEY_SHARE_PRODUCT);
        Gson gson = new Gson();
        Product product = gson.fromJson(productStr, Product.class);
        nameProduct.setText(product.getTitle());
        rating.setText(String.valueOf(product.getRating().getRate()));
        ratingCount.setText(String.valueOf(product.getRating().getCount()));
        description.setText(product.getDescription());
        priceProduct.setText(String.valueOf(product.getPrice()) + " $");
        Glide.with(this).load(product.getImage()).into(imageProduct);
    }
}