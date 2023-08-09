package com.pickleddict.springtodolistbackend.services;

import com.pickleddict.springtodolistbackend.dto.TodoListDto;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.models.User;
import com.pickleddict.springtodolistbackend.repositories.TodoListRepository;
import com.pickleddict.springtodolistbackend.repositories.UserRepository;
import com.pickleddict.springtodolistbackend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {
    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    public void createTodoList(Long userId, TodoList todoList) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.addTodoList(todoList);
        userRepository.save(user);
    }

    public List<TodoList> getAllTodoLists() {
        return todoListRepository.findAll();
    }

    public TodoList getTodoListById(Long id) {
        return todoListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"));
    }

    public TodoList updateTodoList(Long id, TodoListDto todoListDto) {
        TodoList curTodoList = todoListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"));
        curTodoList.setTitle(todoListDto.getTitle());

        return todoListRepository.save(curTodoList);
    }

    public void deleteTodoList(Long id) {
        todoListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TodoList with id " + id + " was not found"));
        todoListRepository.deleteById(id);
    }
}
