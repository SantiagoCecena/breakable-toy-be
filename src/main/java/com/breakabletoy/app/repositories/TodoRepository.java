package com.breakabletoy.app.repositories;

import com.breakabletoy.app.models.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TodoRepository {

    List<Todo> findAll();

    // Get all todos
    List<Todo> findAll(int page, String name, String priority, String done);

    // Get a todo by id
    Optional<Todo> findById(UUID id);

    // Create a new todo
    Todo save(Todo todo);

    // Delete a todo
    void deleteById(UUID id);

    // Update a todo
    Optional<Todo> update(UUID id, Todo todo);

    // Mark todo as done or undone
    Optional<Todo> markTodo(UUID id, boolean done);

    // Mark todo as done
    //    Optional<Todo> markAsDone(UUID id);
    // Mark todo as undone
    //    Optional<Todo> markAsUndone(UUID id);
}
