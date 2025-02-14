package com.breakabletoy.app.services;

import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService implements TodoRepository {

    private TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @Override
    public List<Todo> findAll(int page, String name, String priority, String done) {
        return this.todoRepository.findAll(page, name, priority, done);
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return this.todoRepository.findById(id);
    }

    @Override
    public Todo save(Todo todo) {
        Todo todoToSave = new Todo(todo.getText(), todo.getDueDate(), todo.getPriority());
        return this.todoRepository.save(todoToSave);
    }

    @Override
    public void deleteById(UUID id) {
        this.todoRepository.deleteById(id);
    }

    @Override
    public Optional<Todo> update(UUID id, Todo todo) {
        return this.todoRepository.update(id, todo);
    }

    @Override
    public Optional<Todo> markTodo(UUID id, boolean done) {
        return this.todoRepository.markTodo(id, done);
    }
}
