package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.annotations.ValidJwt;
import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.services.TodoListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/todolist")
public class TodoListController {
    @Autowired
    TodoListService todoListService;

    @PostMapping
    @ValidJwt
    public ResponseEntity<?> createTodoList(@Valid @RequestBody TodoListDto todoListDto) {
        return todoListService.createTodoList(todoListDto);
    }

    @GetMapping("/all")
    @ValidJwt
    public ResponseEntity<?> getAllUserTodoLists() {
        return todoListService.getAllTodoLists();
    }

    @GetMapping
    @ValidJwt
    public ResponseEntity<?> getTodoListById(@RequestParam(name = "id") Long id) {
        return todoListService.getTodoListById(id);
    }

    @PutMapping("/{id}")
    @ValidJwt
    public ResponseEntity<?> updateTodoListById(@PathVariable Long id, @Valid @RequestBody TodoListDto todoListDto) {
        return todoListService.updateTodoList(id, todoListDto);
    }

    @DeleteMapping("/{id}")
    @ValidJwt
    public ResponseEntity<?> deleteTodoListById(@PathVariable Long id) {
        return todoListService.deleteTodoList(id);
    }
}
