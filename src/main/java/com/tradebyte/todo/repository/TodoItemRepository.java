package com.tradebyte.todo.repository;

import com.tradebyte.todo.model.TodoItem;
import com.tradebyte.todo.model.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    /**
     * Finds all items with the given status with pagination.
     *
     * @param status   the status to filter by
     * @param pageable pagination parameters
     * @return page of items with the given status
     */
    Page<TodoItem> findByStatus(TodoStatus status, Pageable pageable);
}