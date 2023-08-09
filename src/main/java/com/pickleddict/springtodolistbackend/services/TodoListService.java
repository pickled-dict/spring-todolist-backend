package com.pickleddict.springtodolistbackend.services;

import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.models.User;
import com.pickleddict.springtodolistbackend.repositories.TodoListRepository;
import com.pickleddict.springtodolistbackend.repositories.UserRepository;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {
    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<MessageResponse> createTodoList(TodoListDto todoListDto) {
        Long userId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJwtTokenFromHeader());
        TodoList todoList =  new TodoList(todoListDto.getTitle());

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.addTodoList(todoList);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Todolist was created successfully"));
    }

    public ResponseEntity<List<TodoList>> getAllTodoLists() {
        return ResponseEntity.ok(todoListRepository.findAll());
    }

    public ResponseEntity<TodoList> getTodoListById(Long id) {
        return ResponseEntity.ok(
                todoListRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"))
        );
    }

    public ResponseEntity<TodoList> updateTodoList(Long id, TodoListDto todoListDto) {
        TodoList curTodoList = todoListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"));
        curTodoList.setTitle(todoListDto.getTitle());

        return ResponseEntity.ok(todoListRepository.save(curTodoList));
    }

    public ResponseEntity<MessageResponse> deleteTodoList(Long id) {
        todoListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"));
        todoListRepository.deleteById(id);

        return ResponseEntity.ok(new MessageResponse("Todo with id " + id + " successfully deleted"));
    }
}
