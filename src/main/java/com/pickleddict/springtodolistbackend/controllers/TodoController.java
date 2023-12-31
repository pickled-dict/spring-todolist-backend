package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.UnexpectedException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TodoController {
    @Autowired
    TodoService todoService;

    @PostMapping("/todolist/{todoListId}/todo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewTodo(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoListId) throws UnexpectedException {
        return todoService.createTodo(todoDto, todoListId);
    }

    @GetMapping("/todo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTodoById(@RequestParam(name = "id") Long todoId) {
        return todoService.getTodoById(todoId);
    }

    @PutMapping("/todo/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateTodoById(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoId) {
        return todoService.updateTodo(todoDto, todoId);
    }

    @DeleteMapping("/todo/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTodoById(@PathVariable Long todoId) {
        return todoService.deleteTodo(todoId);
    }
}
