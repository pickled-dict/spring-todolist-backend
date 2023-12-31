package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.dto.VerifyUserDto;
import com.pickleddict.springtodolistbackend.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        return authenticationService.signupUser(signupRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        return authenticationService.verifyUser(verifyUserDto);
    }
}
