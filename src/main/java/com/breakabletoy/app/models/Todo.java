package com.breakabletoy.app.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Todo {
    private final UUID id;
    private String text;
    private LocalDateTime dueDate;
    private boolean done;
    private LocalDateTime doneDate;
    private String priority;
    private final LocalDateTime createdAt;


    public Todo(String text, LocalDateTime dueDate, String priority) {
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
