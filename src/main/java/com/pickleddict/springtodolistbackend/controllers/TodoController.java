package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.annotations.ValidJwt;
import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TodoController {
    @Autowired
    TodoService todoService;

    @ValidJwt
    @PostMapping("/todolist/{todoListId}/todo")
    public ResponseEntity<?> createNewTodo(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoListId) {
        return todoService.createTodo(todoDto, todoListId);
    }

    @ValidJwt
    @GetMapping("/todo")
    public ResponseEntity<?> getTodoById(@RequestParam(name = "id") Long todoId) {
        return todoService.getTodoById(todoId);
    }

    @ValidJwt
    @PutMapping("/todo/{todoId}")
    public ResponseEntity<?> updateTodoById(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoId) {
        return todoService.updateTodo(todoDto, todoId);
    }

    @ValidJwt
    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> deleteTodoById(@PathVariable Long todoId) {
        return todoService.deleteTodo(todoId);
    }
}
