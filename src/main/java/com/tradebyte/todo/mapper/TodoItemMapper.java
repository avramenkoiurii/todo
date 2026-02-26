package com.tradebyte.todo.mapper;

import com.tradebyte.todo.dto.TodoItemRequest;
import com.tradebyte.todo.dto.TodoItemResponse;
import com.tradebyte.todo.model.TodoItem;

public interface TodoItemMapper {

    TodoItem toEntity(TodoItemRequest request);

    TodoItemResponse toResponse(TodoItem todoItem);
}