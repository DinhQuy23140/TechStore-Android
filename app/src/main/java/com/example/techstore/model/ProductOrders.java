package com.example.techstore.model;

import java.util.Date;
import java.util.List;

public class ProductOrders {
    private String orderId;
    private List<OrderStatus> ordersStatus;
    private String orderDate;
    private String shippingAddress;
    private double totalAmount;
    private List<ProductInCart> products;
//    private boolean isComplete;

    public ProductOrders(String orderDate, String orderId, List<OrderStatus> ordersStatus, List<ProductInCart> products, String shippingAddress, double totalAmount) {
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.ordersStatus = ordersStatus;
        this.products = products;
        this.shippingAddress = shippingAddress;
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderStatus> getOrdersStatus() {
        return ordersStatus;
    }

    public void setOrdersStatus(List<OrderStatus> ordersStatus) {
        this.ordersStatus = ordersStatus;
    }

    public List<ProductInCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInCart> products) {
        this.products = products;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
