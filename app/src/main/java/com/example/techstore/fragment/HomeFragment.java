package com.example.techstore.fragment;

import android.annotation.SuppressLint;
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
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techstore.Adapter.CarouselAdapter;
import com.example.techstore.Adapter.CategoryAdapter;
import com.example.techstore.Adapter.CategoryNameAdapter;
import com.example.techstore.Adapter.FilterAdapter;
import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.RetrofitClient;
import com.example.techstore.R;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ScrollView layout_frg_home;

    ViewPager2 homeFrg_viewPager;
    ArrayList<Integer> listPathImage;
    CarouselAdapter carouselAdapter;

    private ArrayList<Integer> listPathImageCategory;
    private ArrayList<String> listNameCategory;
    CategoryAdapter categoryAdapter;
    RecyclerView homeFrg_rv_category;

    ArrayList<String> listFilter;
    RecyclerView homeFrg_rv_filter, homeFrg_rv_product;
    FilterAdapter adapter;

    CategoryNameAdapter categoryNameAdapter;
    ProductRepository productRepository;
    HomeViewModel homeViewModel;
    ArrayList<Product> listProduct;
    ProductAdapter productAdapter;

    TextView tvSeeAll;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout_frg_home = view.findViewById(R.id.layout_frg_home);
        layout_frg_home.setOnTouchListener(new View.OnTouchListener() {
            float startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endY = event.getY();
                        if (startY - endY > 50) {
                            // Người dùng vuốt lên (kéo xuống)
                            layout_frg_home.post(() -> layout_frg_home.smoothScrollBy(0, 500)); // scroll thêm 500px
                        } else if (endY - startY > 50) {
                            // Vuốt xuống (kéo lên)
                            layout_frg_home.post(() -> layout_frg_home.smoothScrollBy(0, -500));
                        }
                        break;
                }
                return false;
            }
        });

        productRepository = new ProductRepository();
        homeViewModel = new HomeViewModel(productRepository);
        //banner
        homeFrg_viewPager = view.findViewById(R.id.homeFrg_viewPager);

        homeViewModel.loadImgViewPage();
        listPathImage = new ArrayList<>();
        homeViewModel.getListImgViewPage().observe(getViewLifecycleOwner(), listImg -> {
            if (!listImg.isEmpty()) {
                listPathImage.addAll(listImg);
                carouselAdapter = new CarouselAdapter(getContext(), listPathImage);
                homeFrg_viewPager.setAdapter(carouselAdapter);
            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (homeFrg_viewPager.getCurrentItem() + 1) % listPathImage.size();
                homeFrg_viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        //category
        int spanCount = 4;
        int itemPx = dpToPx(getContext(),70);
        homeFrg_rv_category = view.findViewById(R.id.homeFrg_rv_category);
        homeFrg_rv_category.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        homeFrg_rv_category.addItemDecoration(new GridSpacingItemDecoration(spanCount, itemPx));

        homeViewModel.loadCategories();
        listPathImageCategory = new ArrayList<>();
        homeViewModel.getListPathImageCategory().observe(getViewLifecycleOwner(), listPathImage -> {
            listPathImageCategory.addAll(listPathImage);
        });
        listNameCategory = new ArrayList<>();
        homeViewModel.getListNameCategory().observe(getViewLifecycleOwner(), listNameCate -> {
            listNameCategory.addAll(listNameCate);

        });
        categoryAdapter = new CategoryAdapter(getContext(), listNameCategory, listPathImageCategory);
        homeFrg_rv_category.setAdapter(categoryAdapter);

        homeFrg_rv_filter = view.findViewById(R.id.homeFrg_rv_filter);
        homeFrg_rv_filter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryNameAdapter = new CategoryNameAdapter(getContext(), listNameCategory);
        homeFrg_rv_filter.setAdapter(categoryNameAdapter);

        homeFrg_rv_product = view.findViewById(R.id.homeFrg_rv_product);
        homeFrg_rv_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homeFrg_rv_product.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(getContext(), 190)));
        homeViewModel.getProduct();
        homeViewModel.getListProduct().observe(getViewLifecycleOwner(), products -> {
            if (!products.isEmpty()) {
                listProduct = products;
                productAdapter = new ProductAdapter(getContext(), listProduct);
                homeFrg_rv_product.setAdapter(productAdapter);
            }
        });

        tvSeeAll = view.findViewById(R.id.homeFrg_tv_seeAll_mostPopular);
        tvSeeAll.setOnClickListener(viewAll -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, new PopularProductFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}