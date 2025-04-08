package com.example.techstore.model;

public class Rating {
    private float rate;
    private int count;

    public Rating(int count, float rate) {
        this.count = count;
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
