package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
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

    @PostMapping("/todolist/{todoListId}/todo")
    public ResponseEntity<?> createNewTodo(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoListId) {
        todoService.createTodo(todoDto, todoListId);
        return ResponseEntity.ok(new MessageResponse("Todo was created"));
    }

    @GetMapping("/todo")
    public ResponseEntity<?> getTodoById(@RequestParam(name = "id") Long todoId) {
        return ResponseEntity.ok(todoService.getTodoById(todoId));
    }

    @PutMapping("/todo/{todoId}")
    public ResponseEntity<?> updateTodoById(@Valid @RequestBody TodoDto todoDto, @PathVariable Long todoId) {
        return ResponseEntity.ok(todoService.updateTodo(todoDto, todoId));
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> deleteTodoById(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok(new MessageResponse("Todo with id " + todoId + " was successfully deleted"));
    }
}
