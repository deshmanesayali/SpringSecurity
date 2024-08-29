package com.sayali.SpringSecurity.controller;

import com.sayali.SpringSecurity.model.Users;
import com.sayali.SpringSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public Users register(@RequestBody Users newuser){
        System.out.println("here");
        return userService.register(newuser);
    }

    @PostMapping("/jwt-login")
    public String login(@RequestBody Users user){
        System.out.println("here");
        System.out.println(user);
        return userService.verify(user);
    }
}
