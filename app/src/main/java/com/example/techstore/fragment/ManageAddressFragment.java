package com.example.techstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.techstore.Adapter.AddressAdapter;
import com.example.techstore.R;
import com.example.techstore.interfaces.OnItemClickListener;
import com.example.techstore.model.Address;
import com.example.techstore.repository.AddressRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.AddAddressViewModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btnAdd;
    AddAddressViewModel addAddressViewModel;
    AddressRepository addressRepository;
    RecyclerView rvAddress;
    AddressAdapter adapter;
    List<Address> listAddress;
    ImageView btnBack;
    Gson gson;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAddressFragment newInstance(String param1, String param2) {
        ManageAddressFragment fragment = new ManageAddressFragment();
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
        return inflater.inflate(R.layout.fragment_manage_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gson = new Gson();
//        Bundle bundle = getArguments();
        addressRepository = new AddressRepository(getContext());
        addAddressViewModel = new AddAddressViewModel(getContext(), addressRepository);
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(back -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        });

        btnAdd = view.findViewById(R.id.address_add_address);
        btnAdd.setOnClickListener(add -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AddAddressFragment addAddressFragment = new AddAddressFragment();
//            addAddressFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameContainer, addAddressFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        rvAddress = view.findViewById(R.id.rv_address);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        addAddressViewModel.getAllAddress();
        addAddressViewModel.getListAddress().observe(getViewLifecycleOwner(), addresses -> {
            listAddress = addresses;
            adapter = new AddressAdapter(getContext(), listAddress, new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
//                    String strAdress = gson.toJson(listAddress.get(position));
//                    bundle.putString(Constants.KEY_ADDRESS, strAdress);
//                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    CheckOutFragment checkOutFragment = new CheckOutFragment();
//                    checkOutFragment.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.frameContainer, checkOutFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                }
            });
            rvAddress.setAdapter(adapter);
        });
    }
}