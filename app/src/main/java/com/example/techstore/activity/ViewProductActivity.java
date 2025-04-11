package com.example.techstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.techstore.Adapter.ColorAdapter;
import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.untilities.Constants;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity {

    ImageView btnBack, favorite, starRating;
    ImageView imageProduct;
    TextView nameProduct, rating, ratingCount, description, tvPriceProduct, tvQuantity;
    ImageButton btnUp, btnDown;
    LinearLayout addToCart;
    RecyclerView rvColor;
    ColorAdapter colorAdapter;
    List<Integer> colors;
    int defaultQuantity = 1;
    float priceProduct;

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
        tvPriceProduct = findViewById(R.id.priceProduct);
        addToCart = findViewById(R.id.addToCart);
        tvQuantity = findViewById(R.id.tv_quantity);

        Intent intent = getIntent();
        String productStr = intent.getStringExtra(Constants.KEY_SHARE_PRODUCT);
        Gson gson = new Gson();
        Product product = gson.fromJson(productStr, Product.class);
        nameProduct.setText(product.getTitle());
        rating.setText(String.valueOf(product.getRating().getRate()));
        ratingCount.setText(String.valueOf(product.getRating().getCount()));
        description.setText(product.getDescription());
        priceProduct = product.getPrice();
        tvPriceProduct.setText(String.valueOf(priceProduct) + " $");
        Glide.with(this).load(product.getImage()).into(imageProduct);


        btnBack.setOnClickListener(back -> finish());

        btnUp = findViewById(R.id.btn_decrease);
        btnUp.setOnClickListener(up -> {
            defaultQuantity ++;
            setTvQuantity();
        });

        btnDown = findViewById(R.id.btn_increase);
        btnDown.setOnClickListener(down -> {
            if (defaultQuantity > 0) {
                defaultQuantity --;
                setTvQuantity();
            }
        });

        colors = Arrays.asList(
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.gray),
                ContextCompat.getColor(this, R.color.default_background_color),
                ContextCompat.getColor(this, R.color.default_error_message),
                ContextCompat.getColor(this, R.color.bgBottomNavigation),
                ContextCompat.getColor(this, R.color.backgroundItemNotification),
                ContextCompat.getColor(this, R.color.quantity_color)
        );

        colorAdapter = new ColorAdapter(this, colors);
        rvColor = findViewById(R.id.colorRecyclerView);
        rvColor.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvColor.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(this, 40)));
        rvColor.setAdapter(colorAdapter);

    }

    @SuppressLint("SetTextI18n")
    public void setTvQuantity() {
        BigDecimal quantity = new BigDecimal(defaultQuantity);
        BigDecimal price = new BigDecimal(Float.toString(priceProduct));
        BigDecimal total = quantity.multiply(price);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        tvQuantity.setText(String.valueOf(defaultQuantity));
        tvPriceProduct.setText(decimalFormat.format(total) + "$");
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}