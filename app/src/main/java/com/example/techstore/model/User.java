package com.example.techstore.model;

import android.util.Patterns;


public class User {
    private String username, email, password, phone, image;
    private int id;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, int id, String image, String password, String phone, String username) {
        this.email = email;
        this.id = id;
        this.image = image;
        this.password = password;
        this.phone = phone;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean validEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
    }

    public boolean validPassword() {
        return (password.length() >= 8);
    }
}
