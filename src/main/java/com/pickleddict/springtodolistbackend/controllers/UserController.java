package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.annotations.ValidJwt;
import com.pickleddict.springtodolistbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    @ValidJwt
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }
}
