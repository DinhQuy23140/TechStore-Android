package com.example.techstore.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.Adapter.CarouselAdapter;
import com.example.techstore.Adapter.CategoryAdapter;
import com.example.techstore.Adapter.CategoryNameAdapter;
import com.example.techstore.Adapter.FilterAdapter;
import com.example.techstore.Adapter.ProductAdapter;
import com.example.techstore.ApiService.ApiService;
import com.example.techstore.Client.ProductClient;
import com.example.techstore.R;
import com.example.techstore.activity.SearchActivity;
import com.example.techstore.databinding.ActivityMainBinding;
import com.example.techstore.interfaces.OnClickFavorite;
import com.example.techstore.interfaces.OnItemClickListener;
import com.example.techstore.model.Product;
import com.example.techstore.repository.ProductRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.untilities.Decoration;
import com.example.techstore.untilities.GridSpacingItemDecoration;
import com.example.techstore.viewmodel.HomeViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
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

    ActivityMainBinding bindingMain;
    ImageView ivAvatar;
    TextView tvUserName, tvTime;

    NestedScrollView layout_frg_home;
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
    UserRepository userRepository;
    HomeViewModel homeViewModel;
    List<Product> listProduct;
    ProductAdapter productAdapter;
    ConstraintLayout ctrSearch;
    Button btnFavorite, btnNotify;

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
        bindingMain = ActivityMainBinding.inflate(getLayoutInflater());
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //writeToFirebase();
        productRepository = new ProductRepository(getContext());
        userRepository = new UserRepository(getContext());
        homeViewModel = new HomeViewModel(productRepository, userRepository);

        ivAvatar = view.findViewById(R.id.homeFrg_iv_Avt);
        tvUserName = view.findViewById(R.id.homeFrg_tv_Username);
        tvTime = view.findViewById(R.id.homeFrg_tv_time);
        homeViewModel.loadUser();
        homeViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            tvUserName.setText(username);
        });
        homeViewModel.getImg().observe(getViewLifecycleOwner(), img -> {
            if (img != null) {
                byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivAvatar.setImageBitmap(decodedByte);
            } else {
                ivAvatar.setImageResource(R.drawable.background_default_user);
            }
        });
        setTvTime();

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

        ctrSearch = view.findViewById(R.id.ctr_search);
        ctrSearch.setOnClickListener(search -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

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
                if (listPathImage.size() > 0) {
                    int nextItem = (homeFrg_viewPager.getCurrentItem() + 1) % listPathImage.size();
                    homeFrg_viewPager.setCurrentItem(nextItem, true);
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        //category
        int spanCount = 4;
        int itemPx = dpToPx(getContext(),70);
        homeFrg_rv_category = view.findViewById(R.id.homeFrg_rv_category);
        homeFrg_rv_category.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        homeFrg_rv_category.addItemDecoration(new Decoration(spanCount, itemPx));

        homeViewModel.loadCategories();
        listPathImageCategory = new ArrayList<>();
        homeViewModel.getListPathImageCategory().observe(getViewLifecycleOwner(), listPathImage -> {
            listPathImageCategory.addAll(listPathImage);
        });
        listNameCategory = new ArrayList<>();
        homeViewModel.getListNameCategory().observe(getViewLifecycleOwner(), listNameCate -> {
            listNameCategory.addAll(listNameCate);

        });
        categoryAdapter = new CategoryAdapter(getContext(), listNameCategory, listPathImageCategory, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String title = listNameCategory.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_TITLE_SCREEN, title);
                ResultFilterFragment resultFilterFragment = new ResultFilterFragment();
                resultFilterFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, resultFilterFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        homeFrg_rv_category.setAdapter(categoryAdapter);

        homeFrg_rv_filter = view.findViewById(R.id.homeFrg_rv_filter);
        homeFrg_rv_filter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryNameAdapter = new CategoryNameAdapter(getContext(), listNameCategory, position -> {
            String title = listNameCategory.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TITLE_SCREEN, title);
            ResultFilterFragment resultFilterFragment = new ResultFilterFragment();
            resultFilterFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, resultFilterFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        homeFrg_rv_filter.setAdapter(categoryNameAdapter);

        homeFrg_rv_product = view.findViewById(R.id.homeFrg_rv_product);
        homeFrg_rv_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homeFrg_rv_product.addItemDecoration(new GridSpacingItemDecoration(2, 30));
        //homeFrg_rv_product.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeViewModel.loadProduct();
        homeViewModel.getListProduct().observe(getViewLifecycleOwner(), products -> {
            if (!products.isEmpty()) {
                listProduct = products;
                productAdapter = new ProductAdapter(getContext(), listProduct, new OnClickFavorite() {
                    @Override
                    public void onClickFavorite(int position) {
                        homeViewModel.addFavoriteProduct(listProduct.get(position));
                    }

                    @Override
                    public void onClickUnFavorite(int position) {
                        homeViewModel.unFavoriteProduct(listProduct.get(position));
                    }
                });
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

        btnFavorite = view.findViewById(R.id.homeFrg_btn_favorite);
        btnFavorite.setOnClickListener(favorite -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, new ManageFavoriteFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        btnNotify = view.findViewById(R.id.homeFrg_btn_notifi);
        btnNotify.setOnClickListener(notify -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, new NotifyFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTvTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //Toast.makeText(requireContext(), Integer.toString(hour), Toast.LENGTH_SHORT).show();
        if(hour >= 7 && hour <12) {
            tvTime.setText(R.string.setTimeMorining);
        }
        else if(hour >=12 && hour < 18) {
            tvTime.setText(R.string.setTimeAfternoon);
        }
        else if(hour>= 18 && hour < 21) {
            tvTime.setText(R.string.setTimeEvening);
        }
        else {
            tvTime.setText(R.string.setTimeNight);
        }
    }

    public void writeToFirebase() {
        ApiService apiService = ProductClient.getInstance().create(ApiService.class);
        Call<ArrayList<Product>> call = apiService.getProduct();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Gson gson = new Gson();
        call.enqueue(new retrofit2.Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    List<Product> result = response.body();
                    for(Product product : result) {
                        Map<String, Object> bookMap = gson.fromJson(gson.toJson(product), new TypeToken<Map<String, Object>>(){}.getType());
                        firebaseFirestore.collection(Constants.KEY_COLLECTION_PRODUCT)
                                .document(String.valueOf(product.getId()))
                                .set(bookMap);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable throwable) {
            }
        });
    }


}