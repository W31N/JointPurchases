package com.example.jointperchasesnew.service.impl;

import com.example.jointperchasesnew.controller.UserController;
import com.example.jointperchasesnew.controller.UserOrderController;
import com.example.jointperchasesnew.dto.UserRegistrationDto;
import com.example.jointperchasesnew.dto.UserReplenishmentDto;
import com.example.jointperchasesnew.exception.UserAlreadyExistException;
import com.example.jointperchasesnew.exception.UserNotFoundException;
import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.repository.UserRepository;
import com.example.jointperchasesnew.representation.UserRepresentation;
import com.example.jointperchasesnew.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRepresentation getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = getUserData(username);
        UserRepresentation userRepresentation = modelMapper.map(
                user,
                UserRepresentation.class
        );
        addLinks(username, userRepresentation);
        return userRepresentation;
    }

    @Override
    public User getUserData(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        return optionalUser.get();
    }

    @Override
    public UserRepresentation register(UserRegistrationDto userRegistrationDto) {
        User newUser = getUserDataForRegister(userRegistrationDto);
        UserRepresentation userRepresentation = modelMapper.map(
                newUser,
                UserRepresentation.class
        );
        addLinks(newUser.getUsername(), userRepresentation);
        return userRepresentation;
    }

    @Override
    public User getUserDataForRegister(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent())
            throw new UserAlreadyExistException("User already exist");
        User newUser = modelMapper.map(userRegistrationDto, User.class);
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());
        return userRepository.saveAndFlush(newUser);
    }

    @Override
    public UserRepresentation update(UserRegistrationDto userRegistrationDto) {
        return null;
    }

    @Override
    public UserRepresentation deposit(UserReplenishmentDto userReplenishmentDto) {
        User user = getUserDataForDeposit(userReplenishmentDto);
        UserRepresentation userRepresentation = modelMapper.map(
                user, UserRepresentation.class
        );
        return addLinks(user.getUsername(), userRepresentation);
    }

    @Override
    public User getUserDataForDeposit(UserReplenishmentDto userReplenishmentDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userReplenishmentDto.getUsername());
        if (optionalUser.isEmpty())
            throw new UserNotFoundException("User not found");
        User user = optionalUser.get();
        user.deposit(userReplenishmentDto.getDepositAmount());
        return userRepository.saveAndFlush(user);
    }

    @Override
    public CollectionModel<UserRepresentation> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserRepresentation> userRepresentations = users.stream()
                .map(user -> {
                    UserRepresentation representation = modelMapper.map(user, UserRepresentation.class);
                    addLinks(user.getUsername(), representation);
                    return representation;
                })
                .toList();

        return CollectionModel.of(userRepresentations);
    }

    @Override
    public Iterable<User> getAllDataUsers() {
        return userRepository.findAll();
    }


    private UserRepresentation addLinks(String username, UserRepresentation userRepresentation) {
        Link selfLink = linkTo(methodOn(UserController.class).getUser(username)).withSelfRel();
        Link ordersLink = linkTo(methodOn(UserOrderController.class).getUserOrders(username)).withRel("orders");
        userRepresentation.add(selfLink).add(ordersLink);
        return userRepresentation;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
