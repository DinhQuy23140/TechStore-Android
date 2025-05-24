package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techstore.R;
import com.example.techstore.model.Address;
import com.example.techstore.model.District;
import com.example.techstore.model.Province;
import com.example.techstore.model.Ward;
import com.example.techstore.repository.AddressRepository;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.AddAddressViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText edtUsername, edtPhone, edtDetail;
    TextView tvProvince;
    ImageView btnBack;
    RadioGroup rgType;
    Switch swDefault;
    String type = "";
    Button btnAdd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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
        return inflater.inflate(R.layout.fragment_add_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtDetail = view.findViewById(R.id.edt_detail);
        swDefault = view.findViewById(R.id.switch_set_default);
        Bundle bundle = getArguments();

        edtUsername = view.findViewById(R.id.edt_username);
        edtPhone = view.findViewById(R.id.edt_phone);
        tvProvince = view.findViewById(R.id.tv_choose_address);
        if (bundle != null) {
            String province = bundle.getString(Constants.KEY_PROVINCE);
            String district = bundle.getString(Constants.KEY_DISTRICT);
            String ward = bundle.getString(Constants.KEY_WARD);
            if (province != null && district != null && ward != null) {
                String address = province + "\n" + district + "\n" + ward;
                tvProvince.setText(address);
            }
        }
        tvProvince.setOnClickListener(selectProvince -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ChooseProvinceFragment chooseProvinceFragment = new ChooseProvinceFragment();
            chooseProvinceFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameContainer, chooseProvinceFragment);
            fragmentTransaction.commit();
        });

        rgType = view.findViewById(R.id.rg_type);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_home) {
                    type = "Home";
                } else if (checkedId == R.id.rb_office) {
                    type = "Office";
                }
            }
        });

        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(add -> {
//            detail = edtDetail.getText().toString();
//            boolean isDefault = swDefault.isChecked();
//            if (nameProvince.isEmpty() || nameDistrict.isEmpty() || nameWard.isEmpty() || detail.isEmpty() || type.isEmpty()) {
//                Toast.makeText(requireContext(), getString(R.string.all_input_failure), Toast.LENGTH_SHORT).show();
//            } else {
//                addAddressViewModel.addAddress(nameProvince, nameDistrict, nameWard, detail, type, isDefault);
//            }
        });

        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }
}