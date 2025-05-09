package com.example.techstore.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techstore.Adapter.CheckoutAdapter;
import com.example.techstore.R;
import com.example.techstore.databinding.ActivityMainBinding;
import com.example.techstore.model.OrdersStatus;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.CartViewModel;
import com.example.techstore.viewmodel.OrdersViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ActivityMainBinding bindingMain;
    BottomNavigationView bottomNavigationView;
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

    public CheckOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckOutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckOutFragment newInstance(String param1, String param2) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindingMain = ActivityMainBinding.inflate(getLayoutInflater());
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gson = new Gson();
        ordersRepository = new OrdersRepository(getContext());
        ordersViewModel = new OrdersViewModel(ordersRepository);

        userRepository = new UserRepository(getContext());
        cartViewModel = new CartViewModel(userRepository);

        rvCheckout = view.findViewById(R.id.rv_list_order);
        rvCheckout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Bundle bundle = getArguments();
        assert bundle != null;
        String strListProduct = bundle.getString(Constants.KEY_SHARE_PRODUCT);
        Type type = new TypeToken<List<ProductInCart>>() {}.getType();
        listProduct = gson.fromJson(strListProduct, type);
        assert listProduct != null;
        if (!listProduct.isEmpty()) {
            checkoutAdapter = new CheckoutAdapter(getContext(), listProduct);
            rvCheckout.setAdapter(checkoutAdapter);
        }

        ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(back -> {
            getParentFragmentManager().popBackStack();
        });

        btnPay = view.findViewById(R.id.btn_to_payment);
        btnPay.setOnClickListener(pay -> {
            String ordersId = generateRandomId();
            String getCurrentTime = getCurrentDateTime();
            Double total = getTotal(listProduct);
            ProductOrders productOrders = new ProductOrders(getCurrentTime, ordersId, OrdersStatus.PENDING, listProduct, "ADDRESS", total);
            ordersViewModel.addOrders(productOrders);
        });

        ordersViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        bottomNavigationView = bindingMain.bottomNavigation;
        ordersViewModel.getIsSuccess().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, new OrdersFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bottomNavigationView.setSelectedItemId(R.id.nav_bottom_orders);
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