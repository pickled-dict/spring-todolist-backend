package com.pickleddict.springtodolistbackend.services;

import com.pickleddict.springtodolistbackend.dto.JwtResponseDto;
import com.pickleddict.springtodolistbackend.dto.LoginRequestDto;
import com.pickleddict.springtodolistbackend.dto.SignupRequestDto;
import com.pickleddict.springtodolistbackend.dto.VerifyUserDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.User;
import com.pickleddict.springtodolistbackend.repositories.UserRepository;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import com.pickleddict.springtodolistbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<JwtResponseDto> authenticateUser(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getEmail()));
    }

    public ResponseEntity<MessageResponse> signupUser(SignupRequestDto signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail()).ifPresent(user -> {
            throw new BadCredentialsException("Email has already been taken!");
        });

        // create new users account
        User user = new User(signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User was created successfully!"));
    }

    public ResponseEntity<?> verifyUser(VerifyUserDto verifyUserDto) {
        try {
            jwtUtils.validateJwtToken(verifyUserDto.getToken());
            return ResponseEntity.ok(new MessageResponse("user is valid"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("user is not valid"));
        }
    }
}
