package com.example.techstore.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techstore.Adapter.CarouselAdapter;
import com.example.techstore.Adapter.CategoryAdapter;
import com.example.techstore.R;
import com.example.techstore.untilities.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

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

    ViewPager2 homeFrg_viewPager;
    ArrayList<Integer> listPathImage;
    CarouselAdapter carouselAdapter;

    private ArrayList<Integer> listPathImageCategory;
    private ArrayList<String> listNameCategory;
    CategoryAdapter categoryAdapter;
    RecyclerView homeFrg_rv_category;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //banner
        homeFrg_viewPager = view.findViewById(R.id.homeFrg_viewPager);
        listPathImage = new ArrayList<>();
        listPathImage.add(R.drawable.img);
        listPathImage.add(R.drawable.img_1);
        listPathImage.add(R.drawable.img_2);
        listPathImage.add(R.drawable.img_3);
        carouselAdapter = new CarouselAdapter(getContext(), listPathImage);
        homeFrg_viewPager.setAdapter(carouselAdapter);

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
        listPathImageCategory = new ArrayList<>();
        listPathImageCategory.addAll(Arrays.asList(R.drawable.baseline_battery_charging_full_24, R.drawable.baseline_camera_alt_24, R.drawable.baseline_headphones_24,
                R.drawable.baseline_keyboard_24, R.drawable.baseline_laptop_24, R.drawable.baseline_mouse_24, R.drawable.baseline_router_24, R.drawable.baseline_sd_storage_24,
                R.drawable.baseline_smartphone_24, R.drawable.baseline_speaker_24, R.drawable.baseline_tablet_24, R.drawable.baseline_watch_24));
        listNameCategory = new ArrayList<>();
        listNameCategory.addAll(Arrays.asList("Battery change", "Camera", "Headphone", "Keyboard", "Laptop", "Mouse", "Router", "Storage", "Smartphone", "Speaker", "Tablet", "Watch"));
        categoryAdapter = new CategoryAdapter(getContext(), listNameCategory, listPathImageCategory);
        homeFrg_rv_category.setAdapter(categoryAdapter);
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        );
    }
}