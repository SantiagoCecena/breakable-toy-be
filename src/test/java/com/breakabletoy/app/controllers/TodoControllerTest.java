package com.breakabletoy.app.controllers;

import com.breakabletoy.app.models.Priority;
import com.breakabletoy.app.models.Todo;
import com.breakabletoy.app.services.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Todo testTodo;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTodo = new Todo("Test todo", LocalDateTime.of(2025, Month.MARCH, 15, 10, 0), Priority.HIGH);
    }

    @Test
    void getAllTodos_WithDefaultParameters_ShouldReturnTodos() throws Exception {
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoService.findAll(0, "", "all", "all")).thenReturn(todos);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].text").value("Test todo"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        verify(todoService).findAll(0, "", "all", "all");
    }

    @Test
    void getAllTodos_WithCustomParameters_ShouldReturnFilteredTodos() throws Exception {
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoService.findAll(1, "test", "high", "false")).thenReturn(todos);

        mockMvc.perform(get("/api/todos")
                .param("page", "1")
                .param("name", "test")
                .param("priority", "high")
                .param("done", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(todoService).findAll(1, "test", "high", "false");
    }

    @Test
    void getAllTodos_WithEmptyResult_ShouldReturnEmptyArray() throws Exception {
        when(todoService.findAll(0, "", "all", "all")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createTodo_WithValidData_ShouldReturnCreatedTodo() throws Exception {
        when(todoService.save(any(Todo.class))).thenReturn(testTodo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Test todo"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(todoService).save(any(Todo.class));
    }

    @Test
    void createTodo_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Todo invalidTodo = new Todo("", null, null);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTodo_WithShortText_ShouldReturnBadRequest() throws Exception {
        Todo invalidTodo = new Todo("ab", null, Priority.HIGH);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTodo_WithValidIdAndData_ShouldReturnUpdatedTodo() throws Exception {
        Todo updatedTodo = new Todo("Updated todo", null, Priority.MEDIUM);
        when(todoService.update(eq(testId), any(Todo.class))).thenReturn(Optional.of(updatedTodo));

        mockMvc.perform(put("/api/todos/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated todo"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));

        verify(todoService).update(eq(testId), any(Todo.class));
    }

    @Test
    void updateTodo_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(todoService.update(eq(testId), any(Todo.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/todos/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isNotFound());

        verify(todoService).update(eq(testId), any(Todo.class));
    }

    @Test
    void updateTodo_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Todo invalidTodo = new Todo("", null, null);

        mockMvc.perform(put("/api/todos/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void markTodoAsDone_WithValidId_ShouldReturnMarkedTodo() throws Exception {
        Todo doneTodo = new Todo("Test todo", null, Priority.HIGH);
        when(todoService.markTodo(testId, true)).thenReturn(Optional.of(doneTodo));

        mockMvc.perform(post("/api/todos/{id}/done", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test todo"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(todoService).markTodo(testId, true);
    }

    @Test
    void markTodoAsDone_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(todoService.markTodo(testId, true)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/todos/{id}/done", testId))
                .andExpect(status().isNotFound());

        verify(todoService).markTodo(testId, true);
    }

    @Test
    void markTodoAsUndone_WithValidId_ShouldReturnUnmarkedTodo() throws Exception {
        Todo undoneTodo = new Todo("Test todo", null, Priority.HIGH);
        when(todoService.markTodo(testId, false)).thenReturn(Optional.of(undoneTodo));

        mockMvc.perform(put("/api/todos/{id}/undone", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test todo"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(todoService).markTodo(testId, false);
    }

    @Test
    void markTodoAsUndone_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(todoService.markTodo(testId, false)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/todos/{id}/undone", testId))
                .andExpect(status().isNotFound());

        verify(todoService).markTodo(testId, false);
    }

    @Test
    void deleteById_WithValidId_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", testId))
                .andExpect(status().isOk());

        verify(todoService).deleteById(testId);
    }

    @Test
    void deleteById_WithInvalidUUID_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAverages_ShouldReturnAverageMap() throws Exception {
        Map<String, Double> averages = new HashMap<>();
        averages.put("all", 3600000.0);
        averages.put("high", 1800000.0);
        averages.put("medium", 3600000.0);
        averages.put("low", 7200000.0);

        when(todoService.getAllAverages()).thenReturn(averages);

        mockMvc.perform(get("/api/todos/averages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.all").value(3600000.0))
                .andExpect(jsonPath("$.high").value(1800000.0))
                .andExpect(jsonPath("$.medium").value(3600000.0))
                .andExpect(jsonPath("$.low").value(7200000.0));

        verify(todoService).getAllAverages();
    }

    @Test
    void getAverages_WithEmptyResult_ShouldReturnEmptyMap() throws Exception {
        when(todoService.getAllAverages()).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/todos/averages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(todoService).getAllAverages();
    }

    @Test
    void corsConfiguration_ShouldAllowConfiguredOrigins() throws Exception {
        mockMvc.perform(options("/api/todos")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    void getAllTodos_WithInvalidPageParameter_ShouldHandleGracefully() throws Exception {
        when(todoService.findAll(0, "", "all", "all")).thenReturn(Arrays.asList(testTodo));

        mockMvc.perform(get("/api/todos")
                .param("page", "-1"))
                .andExpect(status().isOk());

        verify(todoService).findAll(-1, "", "all", "all");
    }

    @Test
    void createTodo_WithMissingContentType_ShouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/todos")
                .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void createTodo_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }
}