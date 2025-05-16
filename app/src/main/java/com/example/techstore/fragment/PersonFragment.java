package com.example.techstore.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.R;
import com.example.techstore.activity.StartActivity;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.PersonViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    UserRepository userRepository;
    PersonViewModel personViewModel;
    ImageView ivImg, ivEdit, ivProfile, ivAddress, ivNotify, ivPaymemt, ivSercurity, ivLang, ivMode, ivPrivacy, ivHelp, ivInvite;
    TextView tvUsername, tvPhone;
    ConstraintLayout logout;
    SharedPreferences sharedPreferences;
    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userRepository = new UserRepository(getContext());
        personViewModel = new PersonViewModel(userRepository);
        sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ivImg = view.findViewById(R.id.iv_img);
        ivEdit = view.findViewById(R.id.iv_edit);
        tvUsername = view.findViewById(R.id.edt_username);
        tvPhone = view.findViewById(R.id.tv_phone);
        logout = view.findViewById(R.id.logout);

        personViewModel.loadUser();
        personViewModel.getImgUser().observe(getViewLifecycleOwner(), img -> {
            if (img != null) {
                byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivImg.setImageBitmap(decodedByte);
            } else {
                ivImg.setImageResource(R.drawable.background_default_user);
            }
        });
        personViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            tvUsername.setText(username);
        });
        personViewModel.getPhone().observe(getViewLifecycleOwner(), phone -> {
            tvPhone.setText(phone);
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            String title = "";
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.iv_show_profile) {
                    title = Constants.KEY_PROFILE;
                } else if (v.getId() == R.id.iv_show_address) {
                    title = Constants.KEY_ADDRESS;
                } else if (v.getId() == R.id.iv_show_notify) {
                    title = Constants.KEY_NOTIFY;
                } else if (v.getId() == R.id.iv_show_payment) {
                    title = Constants.KEY_PAYMENT;
                } else if (v.getId() == R.id.iv_show_sercurity) {
                    title = Constants.KEY_SECURITY;
                } else if (v.getId() == R.id.iv_show_lang) {
                    title = Constants.KEY_LANGUAGE;
                } else if (v.getId() == R.id.iv_show_mode) {
                    title = Constants.KEY_MODE;
                } else if (v.getId() == R.id.iv_show_privacy) {
                    title = Constants.KEY_PRIVACY;
                } else if (v.getId() == R.id.iv_show_help) {
                    title = Constants.KEY_HELP;
                } else if (v.getId() == R.id.iv_show_invite) {
                    title = Constants.KEY_INVITE;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_TITLE_SCREEN, title);
                ShowMoreFragment showMoreFragment = new ShowMoreFragment();
                showMoreFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, showMoreFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        ivProfile = view.findViewById(R.id.iv_show_profile);
        ivAddress = view.findViewById(R.id.iv_show_address);
        ivNotify = view.findViewById(R.id.iv_show_notify);
        ivPaymemt = view.findViewById(R.id.iv_show_payment);
        ivSercurity = view.findViewById(R.id.iv_show_sercurity);
        ivLang = view.findViewById(R.id.iv_show_lang);
        ivMode = view.findViewById(R.id.iv_show_mode);
        ivPrivacy = view.findViewById(R.id.iv_show_privacy);
        ivHelp = view.findViewById(R.id.iv_show_help);
        ivInvite = view.findViewById(R.id.iv_show_invite);


//        ivProfile.setOnClickListener(onClickListener);
//        ivAddress.setOnClickListener(onClickListener);
        ivNotify.setOnClickListener(onClickListener);
        ivPaymemt.setOnClickListener(onClickListener);
        ivSercurity.setOnClickListener(onClickListener);
        ivMode.setOnClickListener(onClickListener);
        ivPrivacy.setOnClickListener(onClickListener);
        ivHelp.setOnClickListener(onClickListener);
        ivInvite.setOnClickListener(onClickListener);

        ivProfile.setOnClickListener(profile -> {
            replaceFragment(new FillProfileFragment());
        });

        ivAddress.setOnClickListener(address -> {
            replaceFragment(new AddressFragment());
        });

        ivLang.setOnClickListener(lang -> {
            replaceFragment(new LangFragment());
        });

        logout.setOnClickListener(v -> {
            editor.putBoolean(Constants.KEY_IS_LOGIN, false);
            editor.apply();
            Intent intent = new Intent(getActivity(), StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}