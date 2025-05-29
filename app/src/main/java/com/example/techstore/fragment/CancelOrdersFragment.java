package com.example.techstore.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.techstore.Adapter.OrdersAdapter;
import com.example.techstore.Enum.ActionType;
import com.example.techstore.R;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.CartViewModel;
import com.example.techstore.viewmodel.OrdersViewModel;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rvProduct;
    List<ProductInCart> listProduct;
    UserRepository userRepository;
    CartViewModel cartViewModel;
    Gson gson;

    OrdersAdapter ordersAdapter;
    List<ProductOrders> listOrders;
    TextView tvMessage;
    OrdersViewModel ordersViewModel;
    OrdersRepository ordersRepository;


    public CancelOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelOrdersFragment newInstance(String param1, String param2) {
        CancelOrdersFragment fragment = new CancelOrdersFragment();
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
        return inflater.inflate(R.layout.fragment_cancel_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersRepository = new OrdersRepository(requireContext());
        ordersViewModel = new OrdersViewModel(ordersRepository);
        gson = new Gson();
        userRepository = new UserRepository(getContext());
        cartViewModel = new CartViewModel(userRepository);
        tvMessage = view.findViewById(R.id.tv_message);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        rvProduct = view.findViewById(R.id.recyclerCancel);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ordersViewModel.getCancelOrders();
        ordersViewModel.getListCancelOrders().observe(getViewLifecycleOwner(), result -> {
            if (result != null &&!result.isEmpty()) {
                listOrders = new ArrayList<>();
                for (String order : result) {
                    ProductOrders productOrders = gson.fromJson(order, ProductOrders.class);
                    listOrders.add(productOrders);
                }
                Collections.sort(listOrders, (o1, o2) -> {
                    LocalDateTime localDateTime1 = LocalDateTime.parse(o1.getOrderDate(), formatter);
                    LocalDateTime localDateTime2 = LocalDateTime.parse(o2.getOrderDate(), formatter);
                    return localDateTime2.compareTo(localDateTime1);
                });
                ordersAdapter = new OrdersAdapter(getContext(), listOrders, (position, actionType) -> {
                    ProductOrders getProductOrders = listOrders.get(position);
                    String strOrders = gson.toJson(getProductOrders);
                    if (actionType == ActionType.VIEW_STATUS) {
                        TrackOrdersFragment trackOrdersFragment = new TrackOrdersFragment();
                        Bundle bundle = new Bundle();
                        Toast.makeText(getContext(), strOrders, Toast.LENGTH_SHORT).show();
                        bundle.putString(Constants.KEY_SHARE_ORDER, strOrders);
                        trackOrdersFragment.setArguments(bundle);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameContainer, trackOrdersFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else if (actionType == ActionType.SUBMIT) {
                        ordersViewModel.addCompleteOrders(listOrders.get(position));
                        ordersViewModel.deleteOrders(listOrders.get(position));
                        ordersViewModel.getOrders();
                    } else if (actionType == ActionType.CANCEL) {
                        ordersViewModel.addCancelOrders(listOrders.get(position));
                        ordersViewModel.deleteOrders(listOrders.get(position));
                        ordersViewModel.getOrders();
                    }
                });

                rvProduct.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
                rvProduct.setAdapter(ordersAdapter);
            } else {
                rvProduct.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}