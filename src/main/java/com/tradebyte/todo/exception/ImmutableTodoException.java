package com.tradebyte.todo.exception;

public class ImmutableTodoException extends RuntimeException {

    public ImmutableTodoException(Long id) {
        super("Cannot modify past due item with id: " + id);
    }

    public ImmutableTodoException(String message) {
        super(message);
    }
}