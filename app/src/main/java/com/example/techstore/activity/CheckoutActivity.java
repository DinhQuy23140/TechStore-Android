package com.example.techstore.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Adapter.CheckoutAdapter;
import com.example.techstore.R;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.viewmodel.CartViewModel;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvCheckout;
    UserRepository userRepository;
    CartViewModel cartViewModel;
    List<ProductInCart> listProduct;
    CheckoutAdapter checkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);
        cartViewModel = new CartViewModel(userRepository);

        rvCheckout = findViewById(R.id.rv_list_order);
        rvCheckout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartViewModel.getCart();
        cartViewModel.getListProduct().observe(this, productInCarts -> {
            if (!productInCarts.isEmpty()) {
                listProduct = productInCarts;
                checkoutAdapter = new CheckoutAdapter(this, listProduct);
//                rvCheckout.setAdapter(checkoutAdapter);
            }
        });

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(back -> finish());
    }
}