package com.breakabletoy.app.services;

import com.breakabletoy.app.models.Priority;
import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo testTodo;

    @BeforeEach
    void setUp() {
        testTodo = new Todo("Test todo", null, Priority.HIGH);
    }

    @Test
    void save_ShouldCreateNewTodoAndCallRepository() {
        // Given
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.save(testTodo);

        // Then
        assertThat(result).isEqualTo(testTodo);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void getAllAverages_WithCompletedTodos_ShouldCalculateCorrectAverages() {
        // Given
        Todo completedTodo = new Todo("Done task", null, Priority.HIGH);
        completedTodo.setDone(true);
        completedTodo.setDoneDate(LocalDateTime.now());

        when(todoRepository.findAll()).thenReturn(Arrays.asList(completedTodo));

        // When
        Map<String, Double> averages = todoService.getAllAverages();

        // Then
        assertThat(averages).containsKeys("all", "high", "medium", "low");
        assertThat(averages.get("all")).isGreaterThan(0);
        verify(todoRepository, times(4)).findAll();
    }

    @Test
    void getAllAverages_WithNoCompletedTodos_ShouldReturnZeroAverages() {
        // Given
        when(todoRepository.findAll()).thenReturn(Arrays.asList(testTodo));

        // When
        Map<String, Double> averages = todoService.getAllAverages();

        // Then
        assertThat(averages.get("all")).isEqualTo(0.0);
        assertThat(averages.get("high")).isEqualTo(0.0);
    }
}