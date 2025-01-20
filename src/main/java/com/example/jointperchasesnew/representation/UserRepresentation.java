package com.example.jointperchasesnew.representation;

import org.springframework.hateoas.RepresentationModel;

public class UserRepresentation extends RepresentationModel<UserRepresentation> {
    private String username;
    private Double balance;

    public UserRepresentation() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
