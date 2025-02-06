package com.tutorial.entity;

import com.tutorial.entity.TaskPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskPriorityTest {

    @Test
    void testFromId_ValidId() {
        // Test all valid TaskPriority values
        assertEquals(TaskPriority.CRITICAL, TaskPriority.fromId(1));
        assertEquals(TaskPriority.HIGH, TaskPriority.fromId(2));
        assertEquals(TaskPriority.MEDIUM, TaskPriority.fromId(3));
        assertEquals(TaskPriority.LOW, TaskPriority.fromId(4));
        assertEquals(TaskPriority.UNSPECIFIED, TaskPriority.fromId(5));
    }

    @Test
    void testFromId_InvalidId_ShouldThrowException() {
        // Test an invalid TaskPriority ID
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TaskPriority.fromId(99));
        assertEquals("Invalid Taskpriority id: 99", exception.getMessage());
    }
}
