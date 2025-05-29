package com.example.techstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techstore.Adapter.CheckoutAdapter;
import com.example.techstore.Adapter.OrderStatusAdapter;
import com.example.techstore.Adapter.OrdersAdapter;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.OrderStatus;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.OrdersViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView ivImg, ivSearch, ivBack;
    CardView cvColor;
    TextView tvTitle, tvSize, tvQuantity, tvPrice;
    LinearProgressIndicator linearProgressIndicator;
    RecyclerView rvOrderStatus, rvOrders;
    OrderStatusAdapter orderStatusAdapter;
    OrdersViewModel ordersViewModel;
    OrdersRepository ordersRepository;
    CheckoutAdapter ordersAdapter;
    List<ProductInCart> listOrders;
    Gson gson;

    public TrackOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackOrdersFragment newInstance(String param1, String param2) {
        TrackOrdersFragment fragment = new TrackOrdersFragment();
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
        return inflater.inflate(R.layout.fragment_track_orders, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ordersRepository = new OrdersRepository(requireContext());
        ordersViewModel = new OrdersViewModel(ordersRepository);
        gson = new Gson();
        Bundle bundle = getArguments();
        String strProduct = bundle.getString(Constants.KEY_SHARE_PRODUCT);
        ProductInCart product = gson.fromJson(strProduct, ProductInCart.class);
        ivImg = view.findViewById(R.id.iv_img_product);
        ivSearch = view.findViewById(R.id.btn_search);
        cvColor = view.findViewById(R.id.cv_color_product);
        tvTitle = view.findViewById(R.id.tv_cart_title);
        tvSize = view.findViewById(R.id.tv_size_product);
        tvQuantity = view.findViewById(R.id.tv_quantity);
        tvPrice = view.findViewById(R.id.tv_price_total);
        ivBack = view.findViewById(R.id.btn_back);

//        Glide.with(requireContext())
//                        .load(product.getImg())
//                        .error(R.drawable.background_error_load)
//                        .placeholder(R.drawable.background_image_default)
//                        .into(ivImg);
//
//        cvColor.setCardBackgroundColor(product.getColor());
//        tvTitle.setText(product.getTitle());
//        tvSize.setText("Size = " + product.getSize());
//        tvQuantity.setText("Qty = " + product.getQuantity());
//        tvPrice.setText(product.getPrice() + "$");

        ivBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });

        rvOrders = view.findViewById(R.id.rv_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
//        ordersViewModel.getOrders();
//        ordersViewModel.getListOrders().observe(getViewLifecycleOwner(), orders -> {
//            if (!orders.isEmpty()) {
//                listOrders = new ArrayList<>();
//                for (String order : orders) {
//                    ProductOrders productOrders = gson.fromJson(order, ProductOrders.class);
//                    listOrders.add(productOrders);
//                }
//                ordersAdapter = new OrdersAdapter(requireContext(), listOrders, new OnClickWidgetItem() {
//                    @Override
//                    public void onClick(int position) {
//                        ProductOrders productOrders = listOrders.get(position);
//                        TrackOrdersFragment trackOrdersFragment = new TrackOrdersFragment();
//                        Bundle bundle = new Bundle();
//                        String strOrders = gson.toJson(productOrders);
//                        bundle.putString(Constants.KEY_SHARE_ORDER, strOrders);
//                        trackOrdersFragment.setArguments(bundle);
//                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.frameContainer, trackOrdersFragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                    }
//                });
//                rvOrders.setAdapter(ordersAdapter);
//            }
//        });
        String strOrders = bundle.getString(Constants.KEY_SHARE_ORDER);
        ProductOrders productOrders = gson.fromJson(strOrders, ProductOrders.class);
        listOrders = new ArrayList<>();
        listOrders = productOrders.getProducts();
        ordersAdapter = new CheckoutAdapter(getContext(), listOrders);
        rvOrders.setAdapter(ordersAdapter);

        linearProgressIndicator = view.findViewById(R.id.progress_timeline);
        linearProgressIndicator.setProgress(80);
        rvOrderStatus = view.findViewById(R.id.rv_order_status);
        rvOrderStatus.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        List<OrderStatus> statusList = productOrders.getOrdersStatus();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        OrderStatus pendingStatus = statusList.get(0);
        LocalDateTime pendingTime = LocalDateTime.parse(pendingStatus.getTimestamp(), formatter);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(pendingTime, now);
        long hours = duration.toHours();
        if (hours >= 1 && !containsStatus(statusList, getString(R.string.status_confirmed))) {
            statusList.add(new OrderStatus(getString(R.string.status_confirmed), pendingTime.plusHours(1).format(formatter)));
        }
        if (hours >= 2 && !containsStatus(statusList, getString(R.string.status_shipping))) {
            statusList.add(new OrderStatus(getString(R.string.status_shipping), pendingTime.plusHours(2).format(formatter)));
        }
        if (hours >= 12 && !containsStatus(statusList, getString(R.string.status_delivered))) {
            statusList.add(new OrderStatus(getString(R.string.status_delivered), pendingTime.plusHours(12).format(formatter)));
        }

        orderStatusAdapter = new OrderStatusAdapter(requireContext(), statusList);
        rvOrderStatus.setAdapter(orderStatusAdapter);
    }

    private boolean containsStatus(List<OrderStatus> list, String status) {
        for (OrderStatus s : list) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

}