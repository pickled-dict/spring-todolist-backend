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
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.rmi.UnexpectedException;
import java.util.List;

@Service
public class TodoListService {
    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<TodoList> createTodoList(TodoListDto todoListDto) throws UnexpectedException {
        Long userId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJwtTokenFromHeader());
        TodoList newTodoList =  new TodoList(todoListDto.getTitle());

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.addTodoList(newTodoList);
        userRepository.save(user);
        List<TodoList> todoLists =  user.getTodoLists();

        long largestId = Long.MIN_VALUE;
        for (TodoList todoList : todoLists) {
            long currentId = todoList.getId();
            if (currentId > largestId) {
                largestId = currentId;
            }
        }

        TodoList mostRecent = todoListRepository.findById(largestId).orElseThrow(() -> new UnexpectedException("something went wrong"));

        return ResponseEntity.status(HttpStatus.CREATED).body(mostRecent);
    }

    public ResponseEntity<List<TodoList>> getAllUserTodoLists() {
        Long userId = jwtUtils.getUserIdFromJwtToken(jwtUtils.getJwtTokenFromHeader());
        List<TodoList> userTodoLists = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with that id was not found"))
                .getTodoLists();

        return ResponseEntity.ok(userTodoLists);
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
