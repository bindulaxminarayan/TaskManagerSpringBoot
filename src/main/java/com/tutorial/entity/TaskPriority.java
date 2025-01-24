package com.tutorial.entity;

public enum TaskPriority {
    CRITICAL(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4),
    UNSPECIFIED(5);

    private final int id;

    TaskPriority(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TaskPriority fromId(int id) {
        for (TaskPriority priority : values()) {
            if (priority.getId() == id) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid Taskpriority id: " + id);
    }
}
