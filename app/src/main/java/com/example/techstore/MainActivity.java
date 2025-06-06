package com.example.techstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.techstore.activity.StartActivity;
import com.example.techstore.fragment.CartFragment;
import com.example.techstore.fragment.HomeFragment;
import com.example.techstore.fragment.OrdersFragment;
import com.example.techstore.fragment.PersonFragment;
import com.example.techstore.fragment.WalletsFragment;
import com.example.techstore.untilities.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FrameLayout fragmentContainer;
    FrameLayout frameContainer;
    BottomNavigationView bottomNavigationView;
    private static final String SEELECTED_ITEM = "selected_item";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutMain), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
//            v.setPadding(0, systemBars.top, 0, 0);
//            return insets;
//        });

        View layout = findViewById(R.id.frameContainer);
        ViewCompat.setOnApplyWindowInsetsListener(layout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        }

        frameContainer = findViewById(R.id.frameContainer);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            int selectedItemId = savedInstanceState.getInt(SEELECTED_ITEM);
            bottomNavigationView.setSelectedItemId(selectedItemId);
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, new HomeFragment());
            fragmentTransaction.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            if(item.getItemId() == R.id.nav_bottom_home) {
                fragment = new HomeFragment();
                loadFragment(fragment);
                return true;
            } else if(item.getItemId() == R.id.nav_bottom_cart) {
                fragment = new CartFragment();
                loadFragment(fragment);
                return true;
            } else if (item.getItemId() == R.id.nav_bottom_orders) {
                fragment = new OrdersFragment();
                loadFragment(fragment);
                return true;
            } else if (item.getItemId() == R.id.nav_bottom_wallet) {
                fragment = new WalletsFragment();
                loadFragment(fragment);
                return true;
            } else if(item.getItemId() == R.id.nav_bottom_person) {
                fragment = new PersonFragment();
                loadFragment(fragment);
                return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SEELECTED_ITEM, bottomNavigationView.getSelectedItemId());
    }
}