package com.pickleddict.springtodolistbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TodoDto {

    @NotBlank
    @Size(min = 1, message = "must be at least 1 character")
    @Size(max = 5000, message = "must be less than 5000 characters")
    private String content;

    private boolean complete = false;

    public TodoDto() {
    }

    public TodoDto(String content) {
        this.content = content;
    }

    public TodoDto(String content, boolean complete) {
        this.content = content;
        this.complete = complete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "TodoDto{" +
                "content='" + content + '\'' +
                ", complete=" + complete +
                '}';
    }
}
