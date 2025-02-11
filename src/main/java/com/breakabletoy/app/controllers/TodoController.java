package com.breakabletoy.app.controllers;


import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // Get all todos
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAll();
    }

    // Get a todo by id
    @GetMapping("/{id}")
    public Optional<Todo> getTodoById(@PathVariable UUID id) {
        return todoService.findById(id);
    }

    // Create a new todo
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.save(todo);
    }

    // Remove a todo by id
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        todoService.deleteById(id);
    }

    // Update a todo
    @PatchMapping("/{id}")
    public Todo updateTodo(@RequestBody Todo todo) {
        return todoService.update(todo);
    }

}
