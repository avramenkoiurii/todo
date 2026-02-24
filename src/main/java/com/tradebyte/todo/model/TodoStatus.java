package com.tradebyte.todo.model;

public enum TodoStatus {
    /**
     * Item is not yet completed and not past due
     */
    NOT_DONE,

    /**
     * Item has been completed
     */
    DONE,

    /**
     * Item is past its due date and was not completed in time
     */
    PAST_DUE
}