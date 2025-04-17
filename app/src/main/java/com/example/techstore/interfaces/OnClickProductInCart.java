package com.example.techstore.interfaces;

import com.example.techstore.model.ProductInCart;

public interface OnClickProductInCart {
    void onClick(ProductInCart product);
    void onDeteleProductInCart(ProductInCart product);
    void onDecreaseProductInCart(ProductInCart product);
    void onIncreaseProductInCart(ProductInCart product);
}
