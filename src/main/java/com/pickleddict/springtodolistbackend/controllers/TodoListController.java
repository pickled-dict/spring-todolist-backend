package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.dto.SaveTodoListDto;
import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.services.TodoListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.UnexpectedException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/todolist")
public class TodoListController {
    @Autowired
    TodoListService todoListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTodoList(@Valid @RequestBody TodoListDto todoListDto) throws UnexpectedException {
        return todoListService.createTodoList(todoListDto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllUserTodoLists() {
        return todoListService.getAllUserTodoLists();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTodoListById(@RequestParam(name = "id") Long id) {
        return todoListService.getTodoListById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateTodoListById(@PathVariable Long id, @Valid @RequestBody TodoListDto todoListDto) {
        return todoListService.updateTodoList(id, todoListDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTodoListById(@PathVariable Long id) {
        return todoListService.deleteTodoList(id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveTodoList(@Valid @RequestBody SaveTodoListDto todoListDto) {
        return todoListService.saveFullTodoList(todoListDto);
    }
}
