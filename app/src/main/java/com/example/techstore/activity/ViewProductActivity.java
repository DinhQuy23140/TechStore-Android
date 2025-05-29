package com.example.techstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.techstore.Adapter.ColorAdapter;
import com.example.techstore.Adapter.SizeAdapter;
import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.CartViewModel;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ViewProductActivity extends AppCompatActivity {

    CartViewModel cartViewModel;
    UserRepository userRepository;
    ImageView btnBack, favorite, starRating;
    ImageView imageProduct, favoriteBtn;
    TextView nameProduct, rating, ratingCount, description, tvPriceProduct, tvQuantity, typeProduct, sizeProduct;
    ImageButton btnUp, btnDown;
    LinearLayout addToCart;
    RecyclerView rvColor, rvSize;
    ColorAdapter colorAdapter;
    List<Integer> colors;
    List<String> sizes, types;
    SizeAdapter sizeAdapter;
    int defaultQuantity = 1;
    float priceProduct;
    int getColor = 0;
    String getSize = "";
    String type = "";
    String size = "";
    List<String> listSize = new ArrayList<>();
    List<String> listType = new ArrayList<>();

    @SuppressLint("SetTextI18n")
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

        userRepository = new UserRepository(getApplicationContext());
        cartViewModel = new CartViewModel(userRepository);

        typeProduct = findViewById(R.id.typeProduct);
        sizeProduct = findViewById(R.id.sizeProduct);
        btnBack = findViewById(R.id.productBack);
        favoriteBtn = findViewById(R.id.favoriteBtn);
        imageProduct = findViewById(R.id.imgVP);
        favorite = findViewById(R.id.favoriteBtn);
        starRating = findViewById(R.id.starRating);
        nameProduct = findViewById(R.id.productName);
        rating = findViewById(R.id.ratingProduct);
        ratingCount = findViewById(R.id.ratingCount);
        description = findViewById(R.id.desContent);
        tvPriceProduct = findViewById(R.id.priceProduct);
        tvQuantity = findViewById(R.id.tv_quantity);

        Intent intent = getIntent();
        String productStr = intent.getStringExtra(Constants.KEY_SHARE_PRODUCT);
        Gson gson = new Gson();
        Product product = gson.fromJson(productStr, Product.class);
        if (product.isFavorite()) {
            favoriteBtn.setImageResource(R.drawable.icon_favorite_click);
            favoriteBtn.setActivated(true);
        } else {
            favoriteBtn.setImageResource(R.drawable.icon_favorite);
            favoriteBtn.setActivated(false);
        }
        favoriteBtn.setOnClickListener(favorite -> {
            if (!favoriteBtn.isActivated()) {
                userRepository.addFavoriteProduct(product);
                favoriteBtn.setImageResource(R.drawable.icon_favorite_click);
                favoriteBtn.setActivated(true);
            } else {
                userRepository.unFavoriteProduct(product);
                favoriteBtn.setImageResource(R.drawable.icon_favorite);
                favoriteBtn.setActivated(false);
            }
        });
        nameProduct.setText(product.getTitle());
        rating.setText(String.valueOf(product.getRating().getRate()));
        ratingCount.setText(String.valueOf(product.getRating().getCount()));
        description.setText(product.getDescription());
        priceProduct = product.getPrice();
        tvPriceProduct.setText(priceProduct + " $");
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

        if (product.getVariant() != null) {
            type = product.getVariant().getType();
            size = product.getVariant().getSize();
            listSize = product.getVariant().getListSize();
            listType = product.getVariant().getListType();
        } else {
            type = null;
            size = null;
            listSize = null;
            listType = null;
        }

        rvSize = findViewById(R.id.rvSize);
        rvColor = findViewById(R.id.typeRecyclerView);
        if (type != null) {
            typeProduct.setText(type);
        } else typeProduct.setVisibility(View.GONE);

        if (size != null) sizeProduct.setText(size);
        else sizeProduct.setVisibility(View.GONE);

        if (listSize != null) {
            sizes = listSize;
        } else rvSize.setVisibility(View.GONE);

        if (listType != null) {
            types = listType;
        } else rvColor.setVisibility(View.GONE);

//        colorAdapter = new ColorAdapter(this, types, position -> getColor = colors.get(position));
//        rvColor.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
//        rvColor.addItemDecoration(new GridSpacingItemDecoration(5, 10));
//        rvColor.setAdapter(colorAdapter);

        rvSize.setLayoutManager(new GridLayoutManager(this, 4));
        rvSize.addItemDecoration(new GridSpacingItemDecoration(4, 20));
        sizeAdapter = new SizeAdapter(this, sizes, position -> getSize = sizes.get(position));
        rvSize.setAdapter(sizeAdapter);

        addToCart = findViewById(R.id.addToCart);
        addToCart.setOnClickListener(addToCart -> {
//            if (getSize.isEmpty()) {
//                String id = generateRandomId();
//                ProductInCart productInCart = new ProductInCart(getColor, id, product.getId(), product.getImage(), product.getPrice(), defaultQuantity, getSize, product.getTitle());
//                cartViewModel.addOrUpdateCart(productInCart);
//            } else {
//                Toast.makeText(this, "Vui lòng lựa chọn kích thước và màu sắc!", Toast.LENGTH_SHORT).show();
//            }
            if (size != null && getSize ==  "") {
                Toast.makeText(this, "Vui lòng lựa chọn kích thước và màu sắc!", Toast.LENGTH_SHORT).show();
            } else {
                String id = generateRandomId();
                ProductInCart productInCart = new ProductInCart(getColor, id, product.getId(), product.getImage(), product.getPrice(), defaultQuantity, getSize, product.getTitle());
                cartViewModel.addOrUpdateCart(productInCart);
            }
        });
        cartViewModel.getMessage().observe(this, message -> {
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
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

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }
}