package com.breakabletoy.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class Todo {
    private final UUID id;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 255)
    private String text;
    private LocalDateTime dueDate;
    private boolean done;
    private LocalDateTime doneDate;
    @NotNull
    private Priority priority;
    private final LocalDateTime createdAt;


    public Todo(String text, LocalDateTime dueDate, Priority priority) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.dueDate = dueDate;
        this.done = false;
        this.doneDate = null;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
