package com.example.techstore.model;

public class Product {
    private int id;
    private String title;
    private float price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    public Product(String category, String description, int id, String image, float price, Rating rating, String title) {
        this.category = category;
        this.description = description;
        this.id = id;
        this.image = image;
        this.price = price;
        this.rating = rating;
        this.title = title;
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
}
