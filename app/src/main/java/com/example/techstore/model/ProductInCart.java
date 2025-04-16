package com.example.techstore.model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class ProductInCart {
    int id, quantity, color;
    String img, title, size;
    float price;

    public ProductInCart() {
    }

    public ProductInCart(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public ProductInCart(int color, int id, String img, float price, int quantity, String size, String title) {
        this.color = color;
        this.id = id;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductInCart)) return false;
        ProductInCart product = (ProductInCart) obj;
        return id == product.id && size.equals(product.getSize()) && color == product.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, color);
    }
}
