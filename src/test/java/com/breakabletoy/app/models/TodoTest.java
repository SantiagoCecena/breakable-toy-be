package com.breakabletoy.app.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTodo_ShouldPassValidation() {
        // Given
        Todo todo = new Todo("Valid task", null, Priority.HIGH);

        // When
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void todoWithBlankText_ShouldFailValidation() {
        // Given
        Todo todo = new Todo("", null, Priority.HIGH);

        // When
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("text"));
    }

    @Test
    void todoWithShortText_ShouldFailValidation() {
        // Given
        Todo todo = new Todo("ab", null, Priority.HIGH);

        // When
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    void todoConstructor_ShouldSetDefaultValues() {
        // When
        Todo todo = new Todo("Test task", null, Priority.MEDIUM);

        // Then
        assertThat(todo.getId()).isNotNull();
        assertThat(todo.isDone()).isFalse();
        assertThat(todo.getDoneDate()).isNull();
        assertThat(todo.getCreatedAt()).isNotNull();
    }
}