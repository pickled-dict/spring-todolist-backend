package com.pickleddict.springtodolistbackend.services;

import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.http.response.MessageResponse;
import com.pickleddict.springtodolistbackend.models.Todo;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.repositories.TodoListRepository;
import com.pickleddict.springtodolistbackend.repositories.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    TodoRepository todoRepository;

    public ResponseEntity<MessageResponse> createTodo(TodoDto todoDto, Long todoListId) {
        TodoList todoList = todoListRepository.findById(todoListId).orElseThrow(
                () -> new EntityNotFoundException("Todolist with id " + todoListId + " was not found")
        );
        todoList.addTodo(new Todo(todoDto.getContent(), todoDto.isComplete()));
        todoListRepository.save(todoList);

        return ResponseEntity.ok(new MessageResponse("Todo was created"));
    }

    public ResponseEntity<Todo> getTodoById(Long todoId) {
        return ResponseEntity.ok(todoRepository.findById(todoId).orElseThrow(
                () -> new EntityNotFoundException("Todo with id " + todoId + " was not found")
        ));
    }

    public ResponseEntity<Todo> updateTodo(TodoDto todoDto, Long todoId) {
        Todo curTodo = todoRepository.findById(todoId).orElseThrow(
                () -> new EntityNotFoundException("Todo with id " + todoId + " was not found")
        );

        curTodo.setContent(todoDto.getContent());
        curTodo.setComplete(todoDto.isComplete());

        return ResponseEntity.ok(todoRepository.save(curTodo));
    }

    public ResponseEntity<MessageResponse> deleteTodo(Long todoId) {
        todoRepository.findById(todoId).orElseThrow(() -> new EntityNotFoundException("Todo with id " + todoId + " was not found"));
        todoRepository.deleteById(todoId);

        return ResponseEntity.ok(new MessageResponse("Todo with id " + todoId + " was successfully deleted"));
    }
}
