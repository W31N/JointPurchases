package com.example.jointperchasesnew.representation;

import org.springframework.hateoas.RepresentationModel;

public class UserOrderRepresentation extends RepresentationModel<UserOrderRepresentation> {
    private String username;
    private String productName;
    private int quantity;
    private double totalPrice;

    public UserOrderRepresentation() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
