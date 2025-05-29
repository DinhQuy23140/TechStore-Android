package com.example.techstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Adapter.CheckoutAdapter;
import com.example.techstore.R;
import com.example.techstore.model.OrderStatus;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.CartViewModel;
import com.example.techstore.viewmodel.OrdersViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.lang.reflect.Type;

public class CheckoutActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvCheckout;
    UserRepository userRepository;
    CartViewModel cartViewModel;
    List<ProductInCart> listProduct;
    CheckoutAdapter checkoutAdapter;
    Button btnPay;
    Gson gson;
    OrdersRepository ordersRepository;
    OrdersViewModel ordersViewModel;

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

        gson = new Gson();
        ordersRepository = new OrdersRepository(this);
        ordersViewModel = new OrdersViewModel(ordersRepository);

        userRepository = new UserRepository(this);
        cartViewModel = new CartViewModel(userRepository);

        rvCheckout = findViewById(R.id.rv_list_order);
        rvCheckout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Intent intent = getIntent();
        String strListProduct = intent.getStringExtra(Constants.KEY_SHARE_PRODUCT);
        Type type = new TypeToken<List<ProductInCart>>() {}.getType();
        listProduct = gson.fromJson(strListProduct, type);
        assert listProduct != null;
        if (!listProduct.isEmpty()) {
            checkoutAdapter = new CheckoutAdapter(this, listProduct);
            rvCheckout.setAdapter(checkoutAdapter);
        }

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(back -> finish());

        btnPay = findViewById(R.id.btn_to_payment);
        btnPay.setOnClickListener(pay -> {
            String ordersId = generateRandomId();
            String getCurrentTime = getCurrentDateTime();
            Double total = getTotal(listProduct);
            List<OrderStatus> ordersStatuses = new ArrayList<>();
            ordersStatuses.add(new OrderStatus(getString(R.string.status_pending), getCurrentTime));
            ProductOrders productOrders = new ProductOrders(getCurrentTime, ordersId, ordersStatuses, listProduct, "ADDRESS", total);
            ordersViewModel.addOrders(productOrders);
        });

        ordersViewModel.getMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        ordersViewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {

            }
        });
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public String getCurrentDateTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdfDate.format(new Date());
    }

    public double getTotal(List<ProductInCart> listProduct) {
        double total = 0;
        for (ProductInCart product : listProduct) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}