package com.breakabletoy.app.services;

import com.breakabletoy.app.models.Priority;
import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

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

    private double getAverageToComplete(List<Todo> todos) {
        List<Todo> completedTodos = todos.stream()
                .filter(t -> t.getDoneDate() != null)
                .toList();
        if (completedTodos.isEmpty()) return 0;

        long totalMiliseconds = completedTodos.stream()
                .mapToLong(todo -> Duration.between(todo.getCreatedAt(), todo.getDoneDate()).toMillis())
                .sum();

        return (double) totalMiliseconds / completedTodos.size();
    }

    public Map<String, Double> getAllAverages() {
        Map<String, Double> averages = new HashMap<>();
        averages.put("all", getAverageToComplete(this.todoRepository.findAll()));
        averages.put("high", getAverageToComplete(this.todoRepository.findAll().stream().filter(t -> t.getPriority() == Priority.HIGH).toList()));
        averages.put("medium", getAverageToComplete(this.todoRepository.findAll().stream().filter(t -> t.getPriority() == Priority.MEDIUM).toList()));
        averages.put("low", getAverageToComplete(this.todoRepository.findAll().stream().filter(t -> t.getPriority() == Priority.LOW).toList()));
        return averages;
    }
}
