package com.tradebyte.todo.service.impl;

import com.tradebyte.todo.dto.TodoItemRequest;
import com.tradebyte.todo.dto.TodoItemResponse;
import com.tradebyte.todo.exception.ImmutableTodoException;
import com.tradebyte.todo.exception.TodoNotFoundException;
import com.tradebyte.todo.mapper.TodoItemMapper;
import com.tradebyte.todo.model.TodoItem;
import com.tradebyte.todo.model.TodoStatus;
import com.tradebyte.todo.repository.TodoItemRepository;
import com.tradebyte.todo.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;

    @Override
    public TodoItemResponse createTodo(TodoItemRequest request) {
        log.info("Creating todo item: description={}", request.description());

        TodoItem item = mapper.toEntity(request);
        TodoItem saved = repository.save(item);

        log.info("Created todo item: id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    public TodoItemResponse updateDescription(Long id, String description) {
        log.info("Updating description for todo id={}", id);

        TodoItem item = findEntityById(id);

        validateNotPastDue(item, "Cannot update description of past due item");

        item.setDescription(description);
        TodoItem saved = repository.save(item);

        log.info("Updated description for todo id={}", id);
        return mapper.toResponse(saved);
    }

    @Override
    public TodoItemResponse markAsDone(Long id) {
        log.info("Marking todo id={} as done", id);

        TodoItem item = findEntityById(id);

        validateNotPastDue(item, "Cannot mark past due item as done");

        item.setStatus(TodoStatus.DONE);
        item.setDoneDatetime(Instant.now());

        TodoItem saved = repository.save(item);

        log.info("Marked todo id={} as done", id);
        return mapper.toResponse(saved);
    }

    @Override
    public TodoItemResponse markAsNotDone(Long id) {
        log.info("Marking todo id={} as not done", id);

        TodoItem item = findEntityById(id);

        validateNotPastDue(item, "Cannot mark past due item as not done");

        item.setStatus(TodoStatus.NOT_DONE);
        item.setDoneDatetime(null);

        TodoItem saved = repository.save(item);

        log.info("Marked todo id={} as not done", id);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TodoItemResponse findById(Long id) {
        log.debug("Finding todo by id={}", id);
        TodoItem item = findEntityById(id);
        return mapper.toResponse(item);
    }

    @Override
    public Page<TodoItemResponse> findAll(boolean includeAll, Pageable pageable) {
        log.debug("Finding all todos with pagination: includeAll={}, page={}",
                includeAll, pageable.getPageNumber());

        Page<TodoItem> items = includeAll
                ? repository.findAll(pageable)
                : repository.findByStatus(TodoStatus.NOT_DONE, pageable);

        return items.map(mapper::toResponse);
    }


    /**
     * Helper method to find entity by id or throw exception.
     */
    private TodoItem findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    /**
     * Validates that the item is not past due.
     */
    private void validateNotPastDue(TodoItem item, String message) {
        if (item.getStatus() == TodoStatus.PAST_DUE) {
            throw new ImmutableTodoException(message);
        }
    }
}