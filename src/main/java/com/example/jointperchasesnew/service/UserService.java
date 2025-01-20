package com.example.jointperchasesnew.service;

import com.example.jointperchasesnew.dto.UserRegistrationDto;
import com.example.jointperchasesnew.dto.UserReplenishmentDto;
import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.representation.UserRepresentation;
import org.springframework.hateoas.CollectionModel;

public interface UserService {
    UserRepresentation getUser(String username);
    public User getUserData(String username);
    UserRepresentation register(UserRegistrationDto userRegistrationDto);
    public User getUserDataForRegister(UserRegistrationDto userRegistrationDto);
    UserRepresentation update(UserRegistrationDto userRegistrationDto);
    UserRepresentation deposit(UserReplenishmentDto userReplenishmentDto);
    public User getUserDataForDeposit(UserReplenishmentDto userReplenishmentDto);
}
