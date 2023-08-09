package com.pickleddict.springtodolistbackend.services;

import com.pickleddict.springtodolistbackend.dto.TodoDto;
import com.pickleddict.springtodolistbackend.models.Todo;
import com.pickleddict.springtodolistbackend.models.TodoList;
import com.pickleddict.springtodolistbackend.repositories.TodoListRepository;
import com.pickleddict.springtodolistbackend.repositories.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    TodoListRepository todoListRepository;

    @Autowired
    TodoRepository todoRepository;

    public void createTodo(TodoDto todoDto, Long todoListId) {
        TodoList todoList = todoListRepository.findById(todoListId).orElseThrow(
                () -> new EntityNotFoundException("Todolist with id " + todoListId + " was not found")
        );
        todoList.addTodo(new Todo(todoDto.getContent(), todoDto.isComplete()));
        todoListRepository.save(todoList);
    }

    public Todo getTodoById(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new EntityNotFoundException("Todo with id " + todoId + " was not found")
        );
    }

    public Todo updateTodo(TodoDto todoDto, Long todoId) {
        Todo curTodo = todoRepository.findById(todoId).orElseThrow(
                () -> new EntityNotFoundException("Todo with id " + todoId + " was not found")
        );

        curTodo.setContent(todoDto.getContent());
        curTodo.setComplete(todoDto.isComplete());

        return todoRepository.save(curTodo);
    }

    public void deleteTodo(Long todoId) {
        todoRepository.findById(todoId).orElseThrow(() -> new EntityNotFoundException("Todo with id " + todoId + " was not found"));
        todoRepository.deleteById(todoId);
    }
}
