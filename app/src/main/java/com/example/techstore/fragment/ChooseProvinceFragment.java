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
import android.widget.ImageView;
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
 * Use the {@link ChooseProvinceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseProvinceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AddAddressViewModel addAddressViewModel;
    AddressRepository addressRepository;
    UserRepository userRepository;
    List<Province> provinces;
    List<District> districts;
    List<Ward> wards;
    ImageView btnBack;
    Button btnAdd;
    AutoCompleteTextView atvProvince, atvDistrict, atvWard;
    ArrayAdapter adapterProvince, adapterDistrict, adapterWard;
    String nameProvince = "", nameDistrict = "", nameWard = "";
    public ChooseProvinceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseProvinceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseProvinceFragment newInstance(String param1, String param2) {
        ChooseProvinceFragment fragment = new ChooseProvinceFragment();
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
        return inflater.inflate(R.layout.fragment_choose_province, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressRepository = new AddressRepository(getContext());
        addAddressViewModel = new AddAddressViewModel(getContext(), addressRepository);
        provinces = new ArrayList<>();

        atvProvince = view.findViewById(R.id.av_province);
        atvDistrict = view.findViewById(R.id.av_district);
        atvWard = view.findViewById(R.id.av_ward);
        Bundle bundle = getArguments();
        addAddressViewModel.loadAddress();
        addAddressViewModel.getListProvince().observe(getViewLifecycleOwner(), result -> {
            if (!result.isEmpty()) {
                provinces = result;
                adapterProvince = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, provinces);
                atvProvince.setAdapter(adapterProvince);
                //Toast.makeText(requireContext(), Integer.toString(provinces.size()), Toast.LENGTH_SHORT).show();
            }
        });

        atvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province province = provinces.get(position);
                nameProvince = province.getName();
                districts = province.getDistricts();
                adapterDistrict = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts);
                atvDistrict.setAdapter(adapterDistrict);
            }
        });

        atvDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wards = districts.get(position).getWards();
                nameDistrict = districts.get(position).getName();
                adapterWard = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, wards);
                atvWard.setAdapter(adapterWard);
            }
        });

        atvWard.setOnItemClickListener((parent, view1, position, id) -> nameWard = wards.get(position).getName());

        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(add -> {
            if (nameProvince.isEmpty() || nameDistrict.isEmpty() || nameWard.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.all_input_failure), Toast.LENGTH_SHORT).show();
            } else {
//                addAddressViewModel.addAddress(nameProvince, nameDistrict, nameWard, detail, type, isDefault);
                bundle.putString(Constants.KEY_PROVINCE, nameProvince);
                bundle.putString(Constants.KEY_DISTRICT, nameDistrict);
                bundle.putString(Constants.KEY_WARD, nameWard);
                AddAddressFragment addAddressFragment = new AddAddressFragment();
                addAddressFragment.setArguments(bundle);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, addAddressFragment);
                fragmentTransaction.commit();
            }
        });

        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });
    }
}