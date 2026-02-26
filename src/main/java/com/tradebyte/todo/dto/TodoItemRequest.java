package com.tradebyte.todo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

/**
 * Request DTO for creating a new todo item.
 */
public record TodoItemRequest(
        @NotBlank(message = "Description is required")
        @Size(min = 1, max = 5000, message = "Description must be between 1 and 5000 characters")
        String description,

        @NotNull(message = "Due datetime is required")
        @Future(message = "Due datetime must be in the future")
        ZonedDateTime dueDatetime) {
}