package com.example.techstore.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.Adapter.PersonAdapter;
import com.example.techstore.MainActivity;
import com.example.techstore.R;
import com.example.techstore.activity.StartActivity;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.PersonViewModel;

import java.util.List;

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
    ImageView ivImg, ivEdit;
    TextView tvUsername, tvPhone;
    RecyclerView rvFunc;
    PersonAdapter personAdapter;
    List<String> listFunc;
    List<Integer> listPath;
    Button personFrg_btn_logout;
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
        personFrg_btn_logout = view.findViewById(R.id.personFrg_btn_logout);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ivImg = view.findViewById(R.id.iv_img);
        ivEdit = view.findViewById(R.id.iv_edit);
        tvUsername = view.findViewById(R.id.tv_username);
        tvPhone = view.findViewById(R.id.tv_phone);

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

        personFrg_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Constants.KEY_IS_LOGIN, false);
                editor.apply();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}