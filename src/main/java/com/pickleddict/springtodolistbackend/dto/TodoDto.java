package com.pickleddict.springtodolistbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoDto {

    @NotBlank
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
