package com.example.techstore.model;

public class OrderStatus {
    String status, timestamp;

    public OrderStatus(String status, String timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
