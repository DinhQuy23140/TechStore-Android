package com.example.techstore.model;

public class Product {
    private int id;
    private String title;
    private float price;
    private String description;
    private String category;
    private String image;
    private Rating rating;
    private boolean isFavorite;
    private Variant variant;

    public Product(String category, String description, int id, String image, float price, Rating rating, String title, boolean isFavorite) {
        this.category = category;
        this.description = description;
        this.id = id;
        this.image = image;
        this.price = price;
        this.rating = rating;
        this.title = title;
        this.isFavorite = isFavorite;
    }

    public Product(String category, String description, int id, String image, boolean isFavorite, float price, Rating rating, String title, Variant variant) {
        this.category = category;
        this.description = description;
        this.id = id;
        this.image = image;
        this.isFavorite = isFavorite;
        this.price = price;
        this.rating = rating;
        this.title = title;
        this.variant = variant;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
