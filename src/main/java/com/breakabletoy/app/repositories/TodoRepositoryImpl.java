package com.breakabletoy.app.repositories;

import com.breakabletoy.app.models.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private List<Todo> todos = new ArrayList<>();

    @Override
    public List<Todo> findAll() {
        return todos;
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
    }

    @Override
    public Todo save(Todo todo) {
        todos.add(todo);
        return todo;
    }

    @Override
    public void deleteById(UUID id) {
        todos.removeIf(todo -> todo.getId().equals(id));

    }

    @Override
    public Todo update(Todo todo) {
        Optional<Todo> todoToUpdate = findById(todo.getId());
        if (todoToUpdate.isEmpty()) {
            throw new RuntimeException("Todo not found");
        }
        Todo existingTodo = todoToUpdate.get();
        existingTodo.setText(todo.getText());
        existingTodo.setPriority(todo.getPriority());
        existingTodo.setDone(todo.isDone());
        existingTodo.setDueDate(todo.getDueDate());
        return existingTodo;
    }
}
