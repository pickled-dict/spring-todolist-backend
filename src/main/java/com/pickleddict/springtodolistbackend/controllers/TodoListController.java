package com.pickleddict.springtodolistbackend.controllers;

import com.pickleddict.springtodolistbackend.annotations.ValidJwt;
import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import com.pickleddict.springtodolistbackend.services.TodoListService;
import jakarta.validation.Valid;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/todolist")
public class TodoListController {
    @Autowired
    TodoListService todoListService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping
    @ValidJwt
    public ResponseEntity<?> createTodoList(@Valid @RequestBody TodoListDto todoListDto) {
        Long userId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJwtTokenFromHeader());
        todoListService.createTodoList(userId, new TodoList(todoListDto.getTitle()));

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Todolist was created successfully"));
    }

    @GetMapping("/all")
    @ValidJwt
    public ResponseEntity<?> getAllUserTodoLists() {
        return ResponseEntity.ok(todoListService.getAllTodoLists());
    }

    @GetMapping
    @ValidJwt
    public ResponseEntity<?> getTodoListById(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(todoListService.getTodoListById(id));
    }

    @PutMapping("/{id}")
    @ValidJwt
    public ResponseEntity<?> updateTodoListById(@PathVariable Long id, @Valid @RequestBody TodoListDto todoListDto) {
        return ResponseEntity.ok(todoListService.updateTodoList(id, todoListDto));
    }

    @DeleteMapping("/{id}")
    @ValidJwt
    public ResponseEntity<?> deleteTodoListById(@PathVariable Long id) {
        todoListService.deleteTodoList(id);
        return ResponseEntity.ok(new MessageResponse("Todo with id " + id + " successfully deleted"));
    }
}
