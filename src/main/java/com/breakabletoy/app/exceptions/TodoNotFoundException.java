package com.breakabletoy.app.exceptions;

public class TodoNotFoundException extends Exception{
    public TodoNotFoundException(String msg) {
        super(msg);
    }
}
