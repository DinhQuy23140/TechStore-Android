package com.example.techstore.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.techstore.Adapter.CartAdapter;
import com.example.techstore.R;
import com.example.techstore.activity.CheckoutActivity;
import com.example.techstore.interfaces.OnClickCheckBox;
import com.example.techstore.interfaces.OnClickProductInCart;
import com.example.techstore.interfaces.OnClickWidgetItem;
import com.example.techstore.model.ProductInCart;
import com.example.techstore.repository.UserRepository;
import com.example.techstore.untilities.Constants;
import com.example.techstore.viewmodel.CartViewModel;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvCartItem;
    CartAdapter cartAdapter;
    TextView tvTotal;
    LinearLayout lnCheckout;
    List<ProductInCart> listProductInCart, listSelectProductInCart;
    UserRepository userRepository;
    CartViewModel cartViewModel;
    Gson gson = new Gson();

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepository(getContext());
        cartViewModel = new CartViewModel(userRepository);
        tvTotal = view.findViewById(R.id.tv_totalPrice);
        rvCartItem = view.findViewById(R.id.rv_cart_item);
        rvCartItem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cartViewModel.getCart();
        cartViewModel.getListProduct().observe(getViewLifecycleOwner(), listProduct -> {
            if (!listProduct.isEmpty()) {
                listProductInCart = listProduct;
                listSelectProductInCart = new ArrayList<>();
                cartAdapter = new CartAdapter(getContext(), listProductInCart, new OnClickProductInCart() {
                    @Override
                    public void onClick(ProductInCart product) {

                    }

                    @Override
                    public void onDeteleProductInCart(ProductInCart product) {
                        cartViewModel.deleteProduct(product);
                    }

                    @Override
                    public void onDecreaseProductInCart(ProductInCart product) {
                        cartViewModel.updateQuantity(product);
                    }

                    @Override
                    public void onIncreaseProductInCart(ProductInCart product) {
                        cartViewModel.updateQuantity(product);
                    }
                }, new OnClickCheckBox() {
                    @Override
                    public void onCheckBoxClick(int position) {
                        listSelectProductInCart.add(listProductInCart.get(position));
                        tvTotal.setText(totalProduct(listSelectProductInCart));
                    }

                    @Override
                    public void onUnCheckBoxClick(int position) {
                        listSelectProductInCart.remove(listProductInCart.get(position));
                        tvTotal.setText(totalProduct(listSelectProductInCart));
                    }
                });
                rvCartItem.setAdapter(cartAdapter);
            }
        });

        lnCheckout = view.findViewById(R.id.ln_checkout);
        lnCheckout.setOnClickListener(checkout -> {
//            Intent intent = new Intent(getContext(), CheckoutActivity.class);
//            String strListProduct = gson.toJson(listSelectProductInCart);
//            intent.putExtra(Constants.KEY_SHARE_PRODUCT, strListProduct);
//            startActivity(intent);
            String strListProduct = gson.toJson(listSelectProductInCart);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_SHARE_PRODUCT, strListProduct);
            CheckOutFragment checkoutFragment = new CheckOutFragment();
            checkoutFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.frameContainer, checkoutFragment).addToBackStack(null).commit();
        });
    }

    public String totalProduct(List<ProductInCart> listProductInCart) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductInCart product : listProductInCart) {
            BigDecimal quantity = new BigDecimal(product.getQuantity());
            BigDecimal price = new BigDecimal(Float.toString(product.getPrice()));
            BigDecimal totalProduct = quantity.multiply(price);
            total = total.add(totalProduct);
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String totalString = decimalFormat.format(total) + "$";
        return totalString;
    }
}