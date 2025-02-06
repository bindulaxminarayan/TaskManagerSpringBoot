package com.tutorial.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskStatusTest {
    @Test
    void testFromId_ValidId() {
        // Test all valid TaskPriority values
        assertEquals(TaskStatus.NEW, TaskStatus.fromId(1));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromId(2));
        assertEquals(TaskStatus.COMPLETED, TaskStatus.fromId(3));
        assertEquals(TaskStatus.BLOCKED, TaskStatus.fromId(4));
        assertEquals(TaskStatus.DEFERRED, TaskStatus.fromId(5));
    }

    @Test
    void testFromId_InvalidId_ShouldThrowException() {
        // Test an invalid TaskPriority ID
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TaskStatus.fromId(99));
        assertEquals("Invalid TaskStatus id: 99", exception.getMessage());
    }
}
