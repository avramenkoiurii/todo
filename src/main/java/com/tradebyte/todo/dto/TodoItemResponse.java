package com.tradebyte.todo.dto;

import com.tradebyte.todo.model.TodoStatus;

import java.time.Instant;

/**
 * Response DTO for todo item operations.
 */
public record TodoItemResponse(
        Long id,

        String description,

        TodoStatus status,

        Instant creationDatetime,

        Instant dueDatetime,

        Instant doneDatetime) {
}