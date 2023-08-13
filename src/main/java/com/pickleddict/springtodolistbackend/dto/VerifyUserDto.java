package com.pickleddict.springtodolistbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyUserDto {
    @NotBlank
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
