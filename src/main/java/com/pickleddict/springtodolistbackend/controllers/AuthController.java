package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.JwtResponseDto;
import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.models.User;
import com.pickleddict.springtodolistbackend.repositories.UserRepository;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import com.pickleddict.springtodolistbackend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequestDto signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User with that email already exists!"));
        }

        // create new users account
        User user = new User(signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User was created successfully!"));
    }
}
