package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.viewmodel.FavoriteViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FavoriteViewModel favoriteViewModel;
    ProductRepository productRepository;
    ImageView ivBack;
    RecyclerView rvFavorite;
    List<Product> listProduct;
    ProductAdapter productAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageFavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFavoriteFragment newInstance(String param1, String param2) {
        ManageFavoriteFragment fragment = new ManageFavoriteFragment();
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
        return inflater.inflate(R.layout.fragment_manage_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productRepository = new ProductRepository(getContext());
        favoriteViewModel = new FavoriteViewModel(getContext(), productRepository);
        favoriteViewModel.loadFavoriteProduct();
        favoriteViewModel.getFavoriteProducts().observe(getViewLifecycleOwner(), result -> {
//            if (result != null && !result.isEmpty()) {
//                Toast.makeText(getContext(), Integer.toString(result.size()), Toast.LENGTH_SHORT).show();
//            }
            Toast.makeText(getContext(), Integer.toString(result.size()), Toast.LENGTH_SHORT).show();
        });
        ivBack = view.findViewById(R.id.btn_back);
        ivBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }
}