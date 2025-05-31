package com.example.techstore.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.techstore.R;
import com.example.techstore.activity.SearchCommonActivity;
import com.example.techstore.untilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView ivSearch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletsFragment newInstance(String param1, String param2) {
        WalletsFragment fragment = new WalletsFragment();
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
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivSearch = view.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchCommonActivity.class);
            intent.putExtra(Constants.KEY_SEARCH_COMMON, Constants.KEY_COLLECTION_ORDER_COMPLETE);
            startActivity(intent);
        });
    }
}