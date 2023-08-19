package com.pickleddict.springtodolistbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class SaveTodoListDto {

    @NotBlank
    private String title;

    private List<TodoDto> todos = new ArrayList<>();

    public SaveTodoListDto() {};

    public SaveTodoListDto(String title, List<TodoDto> todos) {
        this.title = title;
        this.todos = todos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SaveTodoListDto(List<TodoDto> todos) {
        this.todos = todos;
    }

    public SaveTodoListDto(String title) {
        this.title = title;
    }

    public List<TodoDto> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoDto> todos) {
        this.todos = todos;
    }
}
