package com.example.techstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Adapter.HistoryAdapter;
import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.R;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvHistory, rvResult;
    List<String> listSearch;
    HistoryAdapter historyAdapter;
    TextView tvTitle, tvClear, tvBtnSearch;
    EditText edtSearch;
    HomeViewModel homeViewModel;
    ProductRepository productRepository;
    UserRepository userRepository;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productRepository = new ProductRepository();
        userRepository = new UserRepository(this);
        homeViewModel = new HomeViewModel(productRepository, userRepository);
        tvTitle = findViewById(R.id.tv_title);
        rvHistory = findViewById(R.id.rv_history);
        rvResult = findViewById(R.id.rv_result);
        rvResult.setLayoutManager(new GridLayoutManager(this, 2));
        rvResult.addItemDecoration(new GridSpacingItemDecoration(2, 20));
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listSearch = new ArrayList<>(Arrays.asList(
                "search1", "search2", "search1", "search2",
                "search1", "search2", "search1", "search2",
                "search1", "search2", "search1", "search2"
        ));

        historyAdapter = new HistoryAdapter(this, listSearch);
        rvHistory.setAdapter(historyAdapter);
        tvClear = findViewById(R.id.tv_clear);
        tvClear.setOnClickListener(clear -> {
            listSearch.clear();
            historyAdapter.notifyDataSetChanged();
        });

        edtSearch = findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    tvTitle.setText("Recent");
                    tvClear.setText("Clear All");
                    rvHistory.setVisibility(View.VISIBLE);
                    rvResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvBtnSearch = findViewById(R.id.tv_btn_search);
        tvBtnSearch.setOnClickListener(v -> {
            String strSearch = edtSearch.getText().toString().trim();
            if (!strSearch.isEmpty()) {
                String search = "Results for \"" + strSearch + "\"";
                tvTitle.setText(search);
                rvHistory.setVisibility(View.GONE);
                rvResult.setVisibility(View.VISIBLE);
                homeViewModel.getProduct();
                homeViewModel.getListProduct().observe(this, listProduct -> {
                    if (!listProduct.isEmpty()) {
                        String result = listProduct.size() + " found";
                        tvClear.setText(result);
                        rvResult.setAdapter(new ProductAdapter(this, listProduct));
                    }
                });
            }
        });

    }
}