package com.breakabletoy.app.repositories;

import com.breakabletoy.app.models.Priority;
import com.breakabletoy.app.models.Todo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    //Initialize our "database" and populate with some todos
    private List<Todo> todos = new ArrayList<>();

    {

        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Comprar jabon", null, Priority.MEDIUM));
        todos.add(new Todo("Buy groceries", null, Priority.HIGH));
        todos.add(new Todo("Build next breakable toy", LocalDateTime.of(2025, Month.FEBRUARY, 22, 0, 0), Priority.MEDIUM));
        todos.add(new Todo("Build next breakable toy", LocalDateTime.of(2025, Month.MARCH, 10, 0, 0), Priority.MEDIUM));
        todos.add(new Todo("Build next breakable toy", LocalDateTime.of(2025, Month.MARCH, 10, 0, 0), Priority.MEDIUM));
        todos.add(new Todo("Build next breakable toy", LocalDateTime.of(2025, Month.MARCH, 10, 0, 0), Priority.MEDIUM));
    }

    @Override
    public List<Todo> findAll() {
        return this.todos;
    }

    @Override
    public List<Todo> findAll(int page, String name, String priority, String done) {

        List<Todo> filteredTodos = todos.stream()
                .filter(todo -> todo.getText().toLowerCase().contains(name.toLowerCase().trim()))
                .filter(todo -> priority.trim().equalsIgnoreCase("all") || todo.getPriority().toString().equalsIgnoreCase(priority.trim()))
                .filter(todo -> done.trim().equalsIgnoreCase("all") || todo.isDone() == Boolean.parseBoolean(done))
                .toList();

        // Multiply page times 10 because we want max 10 items in our pagination
        int fromIndex = Math.abs(page * 10);
        int toIndex = Math.min(fromIndex + 10, filteredTodos.size());
        if (fromIndex >= filteredTodos.size()) return Collections.emptyList();
        return filteredTodos.subList(fromIndex, toIndex);
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
    //todo: make the function return a boolean value for a better controller response
    public void deleteById(UUID id) {
        todos.removeIf(todo -> todo.getId().equals(id));
    }

    @Override
    public Optional<Todo> update(UUID id, Todo todo) {
        Optional<Todo> todoToUpdate = findById(id);
        if (todoToUpdate.isEmpty()) {
            return Optional.empty();
        }
        Todo existingTodo = todoToUpdate.get();
        existingTodo.setText(todo.getText() == null ? existingTodo.getText() : todo.getText());
        existingTodo.setPriority(todo.getPriority());
        existingTodo.setDone(todo.isDone());
        existingTodo.setDueDate(todo.getDueDate());
        return Optional.of(existingTodo);
    }

    @Override
    public Optional<Todo> markTodo(UUID id, boolean done) {
        Optional<Todo> todoToDone = findById(id);
        if (todoToDone.isEmpty()) {
            return Optional.empty();
        }
        Todo todo = todoToDone.get();
        todo.setDone(done);
        if (done) {
            todo.setDoneDate(LocalDateTime.now());
        } else {
            todo.setDoneDate(null);
        }
        return Optional.of(todo);
    }
}
