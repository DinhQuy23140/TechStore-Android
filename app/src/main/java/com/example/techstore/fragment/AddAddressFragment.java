package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.techstore.R;
import com.example.techstore.model.District;
import com.example.techstore.model.Province;
import com.example.techstore.model.Ward;
import com.example.techstore.repository.AddressRepository;
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
    AddAddressViewModel addAddressViewModel;
    AddressRepository addressRepository;
    List<Province> provinces;
    List<District> districts;
    List<Ward> wards;
    AutoCompleteTextView avtProvince, avtDistrict, avtWard;
    ArrayAdapter adapterProvince, adapterDistrict, adapterWard;

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
        addressRepository = new AddressRepository();
        addAddressViewModel = new AddAddressViewModel(addressRepository);
        provinces = new ArrayList<>();

        avtProvince = view.findViewById(R.id.av_province);
        avtDistrict = view.findViewById(R.id.av_district);
        avtWard = view.findViewById(R.id.av_ward);
        addAddressViewModel.loadAddress();
        addAddressViewModel.getListAddress().observe(getViewLifecycleOwner(), result -> {
            if (!result.isEmpty()) {
                provinces = result;
                adapterProvince = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, provinces);
                avtProvince.setAdapter(adapterProvince);
                //Toast.makeText(requireContext(), Integer.toString(provinces.size()), Toast.LENGTH_SHORT).show();
            }
        });

        avtProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province province = provinces.get(position);
                districts = province.getDistricts();
                adapterDistrict = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districts);
                avtDistrict.setAdapter(adapterDistrict);
            }
        });

        avtDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wards = districts.get(position).getWards();
                adapterWard = new ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, wards);
                avtWard.setAdapter(adapterWard);
            }
        });

        addressRepository.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}