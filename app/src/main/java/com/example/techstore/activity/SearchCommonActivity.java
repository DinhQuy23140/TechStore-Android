package com.example.techstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Adapter.CartAdapter;
import com.example.techstore.Adapter.OrdersAdapter;
import com.example.techstore.Adapter.OrdersCancelAdapter;
import com.example.techstore.Adapter.OrdersCompletedAdapter;
import com.example.techstore.Enum.ActionType;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickCheckBox;
import com.example.techstore.interfaces.OnClickProductInCart;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.SearchCommonViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchCommonActivity extends AppCompatActivity {

    ImageView ivBack, ivSearch;
    EditText edtSearch;
    RecyclerView rvResult;
    SearchCommonViewModel searchCommonViewModel;
    UserRepository userRepository;
    OrdersRepository ordersRepository;
    String hintSearch = "";
    List<ProductInCart> listProductInCart;
    List<ProductOrders> listOrders;
    CartAdapter cartAdapter;
    OrdersAdapter ordersAdapter;
    OrdersCompletedAdapter ordersCompletedAdapter;
    OrdersCancelAdapter ordersCancelAdapter;
    TextView tvMessage;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_common);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gson = new Gson();
        userRepository = new UserRepository(this);
        ordersRepository = new OrdersRepository(this);
        searchCommonViewModel = new SearchCommonViewModel(userRepository, ordersRepository);
        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);
        edtSearch = findViewById(R.id.edt_search);
        tvMessage = findViewById(R.id.tvMessage);
        rvResult = findViewById(R.id.rv_result);
        rvResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent intent = getIntent();
        String keyFieldSearch = intent.getStringExtra(Constants.KEY_SEARCH_COMMON);
        if (keyFieldSearch != null) {
            switch (keyFieldSearch) {
                case Constants.KEY_COLLECTION_CART: {
                    hintSearch = getString(R.string.search_common_cart);
                    break;
                }
                case Constants.KEY_COLLECTION_ORDER_COMPLETE: {
                    hintSearch = getString(R.string.search_common_orders_completed);
                    break;
                }
                case Constants.KEY_COLLECTION_ORDER_CANCEL: {
                    hintSearch = getString(R.string.search_common_oredrs_cancel);
                    break;
                }
                default: {
                    hintSearch = getString(R.string.search_common_orders);
                    break;
                }
            }
            edtSearch.setHint(hintSearch);
        }

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        ivSearch = findViewById(R.id.iv_search);
        listOrders = new ArrayList<>();
        listProductInCart = new ArrayList<>();
        ivSearch.setOnClickListener(v -> {
            String search = edtSearch.getText().toString();
            if (!search.isEmpty()) {
                switch (keyFieldSearch) {
                    case Constants.KEY_COLLECTION_CART: {
                        searchCommonViewModel.getProductInCartByTitle(search);
                        searchCommonViewModel.getListProductInCart().observe(this, productInCarts -> {
                            if (productInCarts != null && !productInCarts.isEmpty()) {
                                listProductInCart = productInCarts;
                                cartAdapter = new CartAdapter(this, listProductInCart, new OnClickProductInCart() {
                                    @Override
                                    public void onClick(ProductInCart product) {

                                    }

                                    @Override
                                    public void onDeteleProductInCart(ProductInCart product) {

                                    }

                                    @Override
                                    public void onDecreaseProductInCart(ProductInCart product) {

                                    }

                                    @Override
                                    public void onIncreaseProductInCart(ProductInCart product) {

                                    }
                                }, new OnClickCheckBox() {
                                    @Override
                                    public void onCheckBoxClick(int position) {

                                    }

                                    @Override
                                    public void onUnCheckBoxClick(int position) {

                                    }
                                });
                                rvResult.setVisibility(View.VISIBLE);
                                rvResult.setAdapter(cartAdapter);
                                tvMessage.setVisibility(View.GONE);
                            } else {
                                rvResult.setVisibility(View.GONE);
                                tvMessage.setVisibility(View.VISIBLE);
                                tvMessage.setText(getString(R.string.no_product_in_cart));
                            }
                        });
                        break;
                    }
                    case Constants.KEY_NAME_FILED_ORDERS: {
                        searchCommonViewModel.getOrdersByTitle(search);
                        searchCommonViewModel.getListOrders().observe(this, orders -> {
                            if (orders != null && !orders.isEmpty()) {
                                listOrders = orders;
                                ordersAdapter = new OrdersAdapter(this, listOrders, new OnClickWidgetItem() {
                                    @Override
                                    public void onClick(int position, ActionType actionType) {

                                    }
                                });
                                rvResult.setVisibility(View.VISIBLE);
                                rvResult.setAdapter(ordersAdapter);
                                tvMessage.setVisibility(View.GONE);
                            } else {
                                rvResult.setVisibility(View.GONE);
                                tvMessage.setVisibility(View.VISIBLE);
                                tvMessage.setText(getString(R.string.no_product_in_cart));
                            }
                        });
                        Toast.makeText(this, "Orders: " +Integer.toString(listOrders.size()), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.KEY_COLLECTION_ORDER_COMPLETE: {
                        searchCommonViewModel.getOrdersCompleteByTitle(search);
                        searchCommonViewModel.getListOrdersComplete().observe(this, complete -> {
                            if (complete != null && !complete.isEmpty()) {
                                listOrders = complete;
                                ordersCompletedAdapter = new OrdersCompletedAdapter(this, complete, new OnClickWidgetItem() {
                                    @Override
                                    public void onClick(int position, ActionType actionType) {

                                    }
                                });
                                rvResult.setVisibility(View.VISIBLE);
                                rvResult.setAdapter(ordersCompletedAdapter);
                                tvMessage.setVisibility(View.GONE);
                            } else {
                                rvResult.setVisibility(View.GONE);
                                tvMessage.setVisibility(View.VISIBLE);
                                tvMessage.setText(getString(R.string.no_product_in_cart));
                            }
                            //Toast.makeText(this, Integer.toString(complete.size()), Toast.LENGTH_SHORT).show();
                        });
                        Toast.makeText(this, "Compele: " +  Integer.toString(listOrders.size()), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.KEY_COLLECTION_ORDER_CANCEL: {
                        searchCommonViewModel.getOrdersCancelByTitle(search);
                        searchCommonViewModel.getListOrdersCancel().observe(this, cancel -> {
                            Toast.makeText(this, "Cancel: " + Integer.toString(cancel.size()), Toast.LENGTH_SHORT).show();
                            if (cancel != null && !cancel.isEmpty()) {
                                listOrders = cancel;
                                ordersCancelAdapter = new OrdersCancelAdapter(this, cancel, new OnClickWidgetItem() {
                                    @Override
                                    public void onClick(int position, ActionType actionType) {

                                    }
                                });
                                rvResult.setVisibility(View.VISIBLE);
                                rvResult.setAdapter(ordersCancelAdapter);
                                Toast.makeText(this, "Cancel: " + Integer.toString(cancel.size()), Toast.LENGTH_SHORT).show();
                                tvMessage.setVisibility(View.GONE);
                            } else {
                                rvResult.setVisibility(View.GONE);
                                tvMessage.setVisibility(View.VISIBLE);
                                tvMessage.setText(getString(R.string.no_product_in_cart));
                            }
                            //Toast.makeText(this, Integer.toString(cancel.size()), Toast.LENGTH_SHORT).show();
                        });
                        //Toast.makeText(this, "Cancel1: " + Integer.toString(listOrders.size()), Toast.LENGTH_SHORT).show();
                        break;
                    }
//                    default: {
//                        searchCommonViewModel.getOrdersCancelByTitle(search);
//                        searchCommonViewModel.getListOrdersCancel().observe(this, cancelOrders -> {
//                            if (cancelOrders != null && !cancelOrders.isEmpty()) {
////                                listOrders = cancelOrders;
////                                cartAdapter = new CartAdapter(this, listProductInCart, new OnClickProductInCart() {
////                                    @Override
////                                    public void onClick(ProductInCart product) {
////
////                                    }
////
////                                    @Override
////                                    public void onDeteleProductInCart(ProductInCart product) {
////
////                                    }
////
////                                    @Override
////                                    public void onDecreaseProductInCart(ProductInCart product) {
////
////                                    }
////
////                                    @Override
////                                    public void onIncreaseProductInCart(ProductInCart product) {
////
////                                    }
////                                }, new OnClickCheckBox() {
////                                    @Override
////                                    public void onCheckBoxClick(int position) {
////
////                                    }
////
////                                    @Override
////                                    public void onUnCheckBoxClick(int position) {
////
////                                    }
////                                });
//                                rvResult.setVisibility(View.VISIBLE);
//                                //rvResult.setAdapter(cartAdapter);
//                                tvMessage.setVisibility(View.GONE);
//                            } else {
//                                rvResult.setVisibility(View.GONE);
//                                tvMessage.setVisibility(View.VISIBLE);
//                                tvMessage.setText(getString(R.string.no_product_in_cart));
//                            }
//                        });
//                        break;
//                    }
                }
            }
        });
    }
}