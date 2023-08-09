package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        return authenticationService.signupUser(signupRequest);
    }
}
