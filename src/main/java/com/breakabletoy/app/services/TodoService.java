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
    public List<Todo> findAll() {
        return this.todoRepository.findAll();
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return this.todoRepository.findById(id);
    }

    @Override
    public Todo save(Todo todo) {
        return this.todoRepository.save(todo);
    }

    @Override
    public void deleteById(UUID id) {
        this.todoRepository.deleteById(id);
    }

    @Override
    public Todo update(Todo todo) {
        return this.todoRepository.update(todo);
    }
}
