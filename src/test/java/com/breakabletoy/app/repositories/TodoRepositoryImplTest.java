package com.breakabletoy.app.repositories;

import com.breakabletoy.app.models.Priority;
import com.breakabletoy.app.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TodoRepositoryImplTest {

    private TodoRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new TodoRepositoryImpl();
    }

    @Test
    void findAll_WithFiltering_ShouldReturnFilteredResults() {
        // When
        List<Todo> results = repository.findAll(0, "groceries", "high", "all");

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(todo -> todo.getText().toLowerCase().contains("groceries") &&
                todo.getPriority() == Priority.HIGH);
    }

    @Test
    void findAll_WithPagination_ShouldReturnCorrectPage() {
        // When
        List<Todo> page0 = repository.findAll(0, "", "all", "all");
        List<Todo> page1 = repository.findAll(1, "", "all", "all");

        // Then
        assertThat(page0).hasSizeLessThanOrEqualTo(10);
        assertThat(page1).hasSizeLessThanOrEqualTo(10);
    }

    @Test
    void save_ShouldAddTodoToList() {
        // Given
        Todo newTodo = new Todo("New task", null, Priority.MEDIUM);
        int initialSize = repository.findAll().size();

        // When
        Todo saved = repository.save(newTodo);

        // Then
        assertThat(saved).isEqualTo(newTodo);
        assertThat(repository.findAll()).hasSize(initialSize + 1);
    }

    @Test
    void update_WithValidId_ShouldUpdateTodo() {
        // Given
        Todo existingTodo = repository.findAll().get(0);
        UUID id = existingTodo.getId();
        Todo updateData = new Todo("Updated text", null, Priority.LOW);

        // When
        Optional<Todo> result = repository.update(id, updateData);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo("Updated text");
        assertThat(result.get().getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    void markTodo_ShouldUpdateDoneStatus() {
        // Given
        Todo existingTodo = repository.findAll().get(0);
        UUID id = existingTodo.getId();

        // When
        Optional<Todo> result = repository.markTodo(id, true);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().isDone()).isTrue();
        assertThat(result.get().getDoneDate()).isNotNull();
    }

    @Test
    void deleteById_ShouldRemoveTodo() {
        // Given
        Todo existingTodo = repository.findAll().get(0);
        UUID id = existingTodo.getId();
        int initialSize = repository.findAll().size();

        // When
        repository.deleteById(id);

        // Then
        assertThat(repository.findAll()).hasSize(initialSize - 1);
        assertThat(repository.findById(id)).isEmpty();
    }
}