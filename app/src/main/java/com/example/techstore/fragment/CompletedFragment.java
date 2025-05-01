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

import com.example.techstore.Adapter.CartAdapter;
import com.example.techstore.Adapter.OnCompletedAdapter;
import com.example.techstore.Adapter.OnGoingAdapter;
import com.example.techstore.Adapter.OrdersAdapter;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.model.ProductOrders;
import com.example.techstore.repository.OrdersRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.CartViewModel;
import com.example.techstore.viewmodel.OrdersViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvProduct;
    OrdersAdapter ordersAdapter;
    List<ProductOrders> listOrders;
    OrdersViewModel ordersViewModel;
    OrdersRepository ordersRepository;
    Gson gson;

    public CompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedFragment newInstance(String param1, String param2) {
        CompletedFragment fragment = new CompletedFragment();
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
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersRepository = new OrdersRepository(getContext());
        ordersViewModel = new OrdersViewModel(ordersRepository);
        gson = new Gson();
        rvProduct = view.findViewById(R.id.recyclerCompleted);
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listOrders = new ArrayList<>();
        ordersViewModel.getOrders();
        ordersViewModel.getListOrders().observe(getViewLifecycleOwner(), result -> {
            if (!result.isEmpty()) {
                for (String order : result) {
                    ProductOrders productOrders = gson.fromJson(order, ProductOrders.class);
                    listOrders.add(productOrders);
                }
                ordersAdapter = new OrdersAdapter(getContext(), listOrders, new OnClickWidgetItem() {
                    @Override
                    public void onClick(int position) {
                        ProductOrders productOrders = listOrders.get(position);
                        TrackOrdersFragment trackOrdersFragment = new TrackOrdersFragment();
                        Bundle bundle = new Bundle();
                        String strOrders = gson.toJson(productOrders);
                        bundle.putString(Constants.KEY_SHARE_ORDER, strOrders);
                        trackOrdersFragment.setArguments(bundle);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameContainer, trackOrdersFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                rvProduct.setAdapter(ordersAdapter);
            }
        });
    }
}