package com.pickleddict.springtodolistbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoListDto {
    @NotBlank
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
