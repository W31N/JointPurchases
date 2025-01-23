package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.dto.UserRegistrationDto;
import com.example.jointperchasesnew.dto.UserReplenishmentDto;
import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserMutation {
    private UserService userService;

    @DgsMutation
    public User registerUser(@InputArgument("user") UserRegistrationDto userRegistrationDto) {
        return userService.getUserDataForRegister(userRegistrationDto);
    }

    @DgsMutation
    public User depositUser(@InputArgument("user") UserReplenishmentDto userReplenishmentDto) {
        return userService.getUserDataForDeposit(userReplenishmentDto);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}