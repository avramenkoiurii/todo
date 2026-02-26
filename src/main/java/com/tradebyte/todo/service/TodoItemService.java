package com.tradebyte.todo.service;

import com.tradebyte.todo.dto.TodoItemRequest;
import com.tradebyte.todo.dto.TodoItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for todo item operations.
 */
public interface TodoItemService {

    /**
     * Creates a new todo item.
     *
     * @param request the creation request
     * @return created todo item
     */
    TodoItemResponse createTodo(TodoItemRequest request);

    /**
     * Updates the description of a todo item.
     *
     * @param id          the item id
     * @param description new description
     * @return updated todo item
     * @throws com.tradebyte.todo.exception.TodoNotFoundException  if item not found
     * @throws com.tradebyte.todo.exception.ImmutableTodoException if item is past due
     */
    TodoItemResponse updateDescription(Long id, String description);

    /**
     * Marks a todo item as done.
     *
     * @param id the item id
     * @return updated todo item
     * @throws com.tradebyte.todo.exception.TodoNotFoundException  if item not found
     * @throws com.tradebyte.todo.exception.ImmutableTodoException if item is past due
     */
    TodoItemResponse markAsDone(Long id);

    /**
     * Marks a todo item as not done.
     *
     * @param id the item id
     * @return updated todo item
     * @throws com.tradebyte.todo.exception.TodoNotFoundException  if item not found
     * @throws com.tradebyte.todo.exception.ImmutableTodoException if item is past due
     */
    TodoItemResponse markAsNotDone(Long id);

    /**
     * Finds a todo item by id.
     *
     * @param id the item id
     * @return todo item
     * @throws com.tradebyte.todo.exception.TodoNotFoundException if not found
     */
    TodoItemResponse findById(Long id);

    /**
     * Finds all todo items with pagination and optional status filter.
     *
     * @param includeAll if true, returns all items; if false, only NOT_DONE
     * @param pageable   pagination parameters
     * @return page of todo items
     */
    Page<TodoItemResponse> findAll(boolean includeAll, Pageable pageable);
}