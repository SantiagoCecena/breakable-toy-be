package com.breakabletoy.app.repositories;

import com.breakabletoy.app.models.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TodoRepository {

    // Get all todos
    List<Todo> findAll();

    // Get a todo by id
    Optional<Todo> findById(UUID id);

    // Create a new todo
    Todo save(Todo todo);

    // Delete a todo
    void deleteById(UUID id);

    // Update a todo
    Todo update(Todo todo);
}
