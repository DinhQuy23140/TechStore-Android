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
import android.widget.Toast;

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
import com.example.techstore.interfaces.OnClickFavorite;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;
import com.example.techstore.viewmodel.SearchViewModel;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvHistory, rvResult;
    List<String> listSearch;
    HistoryAdapter historyAdapter;
    TextView tvTitle, tvClear, tvBtnSearch;
    EditText edtSearch;
    HomeViewModel homeViewModel;
    ProductRepository productRepository;
    UserRepository userRepository;
    SearchViewModel searchViewModel;
    FirebaseFirestore firestore;
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

        firestore = FirebaseFirestore.getInstance();
        productRepository = new ProductRepository(this);
        userRepository = new UserRepository(this);
        searchViewModel = new SearchViewModel(userRepository, productRepository);
        homeViewModel = new HomeViewModel(productRepository, userRepository);
        tvTitle = findViewById(R.id.tv_title);
        rvHistory = findViewById(R.id.rv_history);
        rvResult = findViewById(R.id.rv_result);
        rvResult.setLayoutManager(new GridLayoutManager(this, 2));
        rvResult.addItemDecoration(new GridSpacingItemDecoration(2, 20));
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        listSearch = new ArrayList<>(Arrays.asList(
//                "search1", "search2", "search1", "search2",
//                "search1", "search2", "search1", "search2",
//                "search1", "search2", "search1", "search2"
//        ));

        searchViewModel.getHistory();
        searchViewModel.getSearch().observe(this, rvResult -> {
            listSearch = rvResult;
            historyAdapter = new HistoryAdapter(this, listSearch, new OnClickWidgetItem() {
                @Override
                public void onClick(int position) {
                    String history = listSearch.get(position);
                    searchViewModel.deleteHistory(history);
                    searchViewModel.getHistory();
                }
            });
            rvHistory.setAdapter(historyAdapter);

        });

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
                    searchViewModel.getHistory();
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
                searchViewModel.addSearch(strSearch);
                rvHistory.setVisibility(View.GONE);
                rvResult.setVisibility(View.VISIBLE);
                searchViewModel.getProductByTitle(strSearch);
                searchViewModel.getListProduct().observe(this, listSearch -> {
                    if (listSearch != null && !listSearch.isEmpty()) {
                        String result = listSearch.size() + " found";
                        tvClear.setText(result);
                        Toast.makeText(this, Integer.toString(listSearch.size()), Toast.LENGTH_SHORT).show();
                        rvResult.setAdapter(new ProductAdapter(this, listSearch, new OnClickFavorite() {
                            @Override
                            public void onClickFavorite(int position) {
                                searchViewModel.addFavoriteProduct(listSearch.get(position));
                            }

                            @Override
                            public void onClickUnFavorite(int position) {
                                searchViewModel.unFavoriteProduct(listSearch.get(position));
                            }
                        }));
                    }
                });
            }
        });

    }
}