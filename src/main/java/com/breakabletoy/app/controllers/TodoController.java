package com.breakabletoy.app.controllers;


import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
public class TodoController {

    @Autowired
    private TodoService todoService;

    // Get all todos
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "all") String priority,
            @RequestParam(defaultValue = "all") String done
    ) {
        return ResponseEntity.ok(todoService.findAll(page, name, priority, done));
    }

    // Create a new todo
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.save(todo));
    }

    // Update a todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable UUID id, @Valid @RequestBody Todo todo) {
        Optional<Todo> updatedTodo = todoService.update(id, todo);
        if (updatedTodo.isPresent()) {
            return ResponseEntity.ok(updatedTodo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Mark todo as done
    @PostMapping("/{id}/done")
    public ResponseEntity<Todo> markTodoAsDone(@PathVariable UUID id) {
        Optional<Todo> markedTodo = todoService.markTodo(id, true);
        if (markedTodo.isPresent()) {
            return ResponseEntity.ok(markedTodo.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // Mark todo as undone
    @PutMapping("/{id}/undone")
    public ResponseEntity<Todo> markTodoAsUndone(@PathVariable UUID id) {
        Optional<Todo> markedTodo = todoService.markTodo(id, false);
        if (markedTodo.isPresent()) {
            return ResponseEntity.ok(markedTodo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Remove a todo by id
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        todoService.deleteById(id);
    }

    // Get average time
    @GetMapping("/averages")
    public ResponseEntity<Map<String, Double>> getAverages() {
        Map<String, Double> averages = todoService.getAllAverages();
        return ResponseEntity.ok(averages);
    }

}
