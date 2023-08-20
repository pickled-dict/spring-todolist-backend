package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        return ResponseEntity.ok(new MessageResponse("Everyone gets a car"));
    }

    @GetMapping("/hasjwt")
    public ResponseEntity<?> jwtAccess() {
        return ResponseEntity.ok(new MessageResponse("Restricted Response"));
    }
}
