package com.pickleddict.springtodolistbackend.repositories;

import com.pickleddict.springtodolistbackend.models.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
}
