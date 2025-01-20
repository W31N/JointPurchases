package com.example.jointperchasesnew.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDto {
    private String username;
    private String password;

    @NotBlank(message = "Username can't be blank")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "Password can't be blank")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
