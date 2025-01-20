package com.example.jointperchasesnew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class UserReplenishmentDto {
    private String username;
    private Double depositAmount;

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Positive(message = "Deposit amount must be bigger than 0")
    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }
}