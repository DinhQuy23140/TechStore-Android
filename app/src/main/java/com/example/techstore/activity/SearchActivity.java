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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.Adapter.HistoryAdapter;
import com.example.techstore.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvHistory;
    List<String> listSearch;
    HistoryAdapter historyAdapter;
    TextView tvTitle, tvClear;
    EditText edtSearch;
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

        tvTitle = findViewById(R.id.tv_title);
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String search = "Results for \"" + edtSearch.getText().toString() + "\"";
                tvTitle.setText(search);  // Cập nhật tiêu đề kết quả tìm kiếm
                tvClear.setText("Clear"); // Chỉnh lại nút Clear

                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listSearch = new ArrayList<>(Arrays.asList(
                "search1", "search2", "search1", "search2",
                "search1", "search2", "search1", "search2",
                "search1", "search2", "search1", "search2"
        ));

        historyAdapter = new HistoryAdapter(this, listSearch);
        rvHistory.setAdapter(historyAdapter);
    }
}