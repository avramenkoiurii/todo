package com.tradebyte.todo.service.impl;

import com.tradebyte.todo.dto.TodoItemRequest;
import com.tradebyte.todo.dto.TodoItemResponse;
import com.tradebyte.todo.exception.ImmutableTodoException;
import com.tradebyte.todo.exception.TodoNotFoundException;
import com.tradebyte.todo.mapper.TodoItemMapper;
import com.tradebyte.todo.model.TodoItem;
import com.tradebyte.todo.model.TodoStatus;
import com.tradebyte.todo.repository.TodoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService Unit Tests")
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository repository;

    @Mock
    private TodoItemMapper mapper;

    @InjectMocks
    private TodoItemServiceImpl todoService;

    private TodoItem testItem;
    private TodoItemResponse testResponse;

    @BeforeEach
    void setUp() {
        testItem = TodoItem.builder()
                .id(1L)
                .description("Test task")
                .status(TodoStatus.NOT_DONE)
                .creationDatetime(Instant.now())
                .dueDatetime(Instant.now().plusSeconds(86400))
                .build();

        testResponse = new TodoItemResponse(
                1L,
                "Test task",
                TodoStatus.NOT_DONE,
                null,
                null,
                null);
    }

    @Test
    @DisplayName("Should create todo item successfully")
    void shouldCreateTodoSuccessfully() {
        // Given
        TodoItemRequest request = new TodoItemRequest(
                "New task",
                ZonedDateTime.now().plusDays(1));

        when(mapper.toEntity(request)).thenReturn(testItem);
        when(repository.save(testItem)).thenReturn(testItem);
        when(mapper.toResponse(testItem)).thenReturn(testResponse);

        // When
        TodoItemResponse result = todoService.createTodo(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(testItem);
    }

    @Test
    @DisplayName("Should update description successfully for not done item")
    void shouldUpdateDescriptionSuccessfully() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(testItem)).thenReturn(testItem);
        when(mapper.toResponse(testItem)).thenReturn(testResponse);

        // When
        TodoItemResponse result = todoService.updateDescription(1L, "Updated description");

        // Then
        assertThat(result).isNotNull();
        assertThat(testItem.getDescription()).isEqualTo("Updated description");
        verify(repository).save(testItem);
    }

    @Test
    @DisplayName("Should throw exception when updating description of past due item")
    void shouldThrowExceptionWhenUpdatingPastDueItem() {
        // Given
        testItem.setStatus(TodoStatus.PAST_DUE);
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        // When & Then
        assertThatThrownBy(() -> todoService.updateDescription(1L, "New description"))
                .isInstanceOf(ImmutableTodoException.class)
                .hasMessageContaining("Cannot update description of past due item");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should mark item as done successfully")
    void shouldMarkAsDoneSuccessfully() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(testItem)).thenReturn(testItem);
        when(mapper.toResponse(testItem)).thenReturn(testResponse);

        // When
        TodoItemResponse result = todoService.markAsDone(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(testItem.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(testItem.getDoneDatetime()).isNotNull();
        verify(repository).save(testItem);
    }

    @Test
    @DisplayName("Should throw exception when marking past due item as done")
    void shouldThrowExceptionWhenMarkingPastDueAsDone() {
        // Given
        testItem.setStatus(TodoStatus.PAST_DUE);
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        // When & Then
        assertThatThrownBy(() -> todoService.markAsDone(1L))
                .isInstanceOf(ImmutableTodoException.class)
                .hasMessageContaining("Cannot mark past due item as done");
    }

    @Test
    @DisplayName("Should mark item as not done successfully")
    void shouldMarkAsNotDoneSuccessfully() {
        // Given
        testItem.setStatus(TodoStatus.DONE);
        testItem.setDoneDatetime(Instant.now());

        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(testItem)).thenReturn(testItem);
        when(mapper.toResponse(testItem)).thenReturn(testResponse);

        // When
        TodoItemResponse result = todoService.markAsNotDone(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(testItem.getStatus()).isEqualTo(TodoStatus.NOT_DONE);
        assertThat(testItem.getDoneDatetime()).isNull();
        verify(repository).save(testItem);
    }

    @Test
    @DisplayName("Should throw exception when item not found")
    void shouldThrowExceptionWhenItemNotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.findById(999L))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessageContaining("Todo item not found with id: 999");
    }
}