package com.tradebyte.todo.mapper.impl;

import com.tradebyte.todo.dto.TodoItemRequest;
import com.tradebyte.todo.dto.TodoItemResponse;
import com.tradebyte.todo.mapper.TodoItemMapper;
import com.tradebyte.todo.model.TodoItem;
import com.tradebyte.todo.model.TodoStatus;

import java.time.Instant;

public class TodoItemMapperImpl implements TodoItemMapper {

    @Override
    public TodoItem toEntity(TodoItemRequest request) {
        Instant now = Instant.now();

        return TodoItem.builder()
                .description(request.description())
                .status(TodoStatus.NOT_DONE)
                .creationDatetime(now)
                .dueDatetime(request.dueDatetime().toInstant())
                .build();
    }

    @Override
    public TodoItemResponse toResponse(TodoItem todoItem) {
        return new TodoItemResponse(
                todoItem.getId(),
                todoItem.getDescription(),
                todoItem.getStatus(),
                todoItem.getCreationDatetime(),
                todoItem.getDueDatetime(),
                todoItem.getDoneDatetime());
    }
}