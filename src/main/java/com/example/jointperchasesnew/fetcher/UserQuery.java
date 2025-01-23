package com.example.jointperchasesnew.fetcher;

import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class UserQuery {
    private UserService userService;

    @DgsQuery
    public User user(@InputArgument(name = "username") String username){
        return userService.getUserData(username);
    }

    @DgsQuery
    public Iterable<User> allUsers(){
        return userService.getAllDataUsers();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
