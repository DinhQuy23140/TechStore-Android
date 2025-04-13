package com.example.techstore.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.Adapter.CategoryNameAdapter;
import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.R;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ProductRepository productRepository;
    UserRepository userRepository;
    HomeViewModel homeViewModel;
    ImageView btnBack, btnSearch;
    TextView tvTitle;
    RecyclerView rvFilter, rvProduct;
    CategoryNameAdapter categoryNameAdapter;
    ProductAdapter productAdapter;
    ArrayList<String> listFilter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PopularProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularProductFragment newInstance(String param1, String param2) {
        PopularProductFragment fragment = new PopularProductFragment();
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
        return inflater.inflate(R.layout.fragment_popular_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productRepository = new ProductRepository();
        userRepository = new UserRepository(getContext());
        homeViewModel = new HomeViewModel(productRepository, userRepository);
        btnBack = view.findViewById(R.id.btn_back);
        btnSearch = view.findViewById(R.id.btn_search);
        tvTitle = view.findViewById(R.id.tv_title);
        rvFilter = view.findViewById(R.id.rv_filter);
        rvProduct = view.findViewById(R.id.rv_product);

        rvFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listFilter = new ArrayList<>();
        homeViewModel.loadCategories();
        homeViewModel.getListNameCategory().observe(getViewLifecycleOwner(), filters -> {
            if (!filters.isEmpty()) {
                listFilter.addAll(filters);
                categoryNameAdapter = new CategoryNameAdapter(getContext(), listFilter);
                rvFilter.setAdapter(categoryNameAdapter);
            }
        });

        homeViewModel.getProduct();
        homeViewModel.getListProduct().observe(getViewLifecycleOwner(), listProduct -> {
            if (!listProduct.isEmpty()) {
                productAdapter = new ProductAdapter(getContext(), listProduct);
                rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rvProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(getContext(), 190)));
                rvProduct.setAdapter(productAdapter);
            }
        });

        btnBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }
    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }
}