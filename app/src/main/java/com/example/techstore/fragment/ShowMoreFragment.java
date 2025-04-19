package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.techstore.R;
import com.example.techstore.untilities.Constants;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowMoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvTitle;

    public ShowMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowMoreFragment newInstance(String param1, String param2) {
        ShowMoreFragment fragment = new ShowMoreFragment();
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
        return inflater.inflate(R.layout.fragment_show_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        String title = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            switch (Objects.requireNonNull(bundle.getString(Constants.KEY_TITLE_SCREEN))) {
                case Constants.KEY_PROFILE: {
                    title = getString(R.string.person_profile);
                    break;
                }
                case Constants.KEY_ADDRESS: {
                    title = getString(R.string.person_address);
                    break;
                }
                case Constants.KEY_NOTIFY: {
                    title = getString(R.string.person_notifi);
                    break;
                }
                case Constants.KEY_PAYMENT: {
                    title = getString(R.string.person_payment);
                    break;
                }
                case Constants.KEY_SECURITY: {
                    title = getString(R.string.person_sercurity);
                    break;
                }
                case Constants.KEY_LANGUAGE: {
                    title = getString(R.string.person_lang);
                    break;
                }
                case Constants.KEY_MODE: {
                    title = getString(R.string.person_dark);
                    break;
                }
                case Constants.KEY_PRIVACY: {
                    title = getString(R.string.person_privacy);
                    break;
                }
                case Constants.KEY_HELP: {
                    title = getString(R.string.person_help);
                    break;
                }
                case Constants.KEY_INVITE: {
                    title = getString(R.string.person_invite);
                    break;
                }
            }
            tvTitle.setText(title);
        }
    }


}