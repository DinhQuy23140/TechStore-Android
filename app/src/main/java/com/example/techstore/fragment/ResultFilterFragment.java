package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnClickFavorite;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView ivBack, ivSearch;
    TextView tvTitle;
    RecyclerView rvProduct;
    List<Product> resultProduct;
    ProductAdapter productAdapter;
    ProductRepository productRepository;
    UserRepository userRepository;
    HomeViewModel homeViewModel;

    public ResultFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFilterFragment newInstance(String param1, String param2) {
        ResultFilterFragment fragment = new ResultFilterFragment();
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
        return inflater.inflate(R.layout.fragment_result_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productRepository = new ProductRepository(getContext());
        userRepository = new UserRepository(getContext());
        homeViewModel = new HomeViewModel(productRepository, userRepository);
        ivBack = view.findViewById(R.id.iv_back);
        ivSearch = view.findViewById(R.id.iv_search);
        tvTitle = view.findViewById(R.id.tv_title);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString(Constants.KEY_TITLE_SCREEN);
            tvTitle.setText(title);
        }

        rvProduct = view.findViewById(R.id.rv_product);
        rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProduct.addItemDecoration(new GridSpacingItemDecoration(2, 20));

        homeViewModel.getListProduct().observe(getViewLifecycleOwner(), listPoroduct -> {
            if (!listPoroduct.isEmpty()) {
                resultProduct = listPoroduct;
                productAdapter = new ProductAdapter(getContext(), resultProduct, new OnClickFavorite() {
                    @Override
                    public void onClickFavorite(int position) {
                        userRepository.addFavoriteProduct(resultProduct.get(position));
                    }

                    @Override
                    public void onClickUnFavorite(int position) {
                        userRepository.unFavoriteProduct(resultProduct.get(position));
                    }
                });
                rvProduct.setAdapter(productAdapter);
            }
        });

        ivBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }
}