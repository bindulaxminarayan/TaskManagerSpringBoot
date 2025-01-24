package com.tutorial.entity;

public enum TaskStatus {
    NEW(1),
    IN_PROGRESS(2),
    COMPLETED(3);

    private final int id;

    TaskStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TaskStatus fromId(int id) {
        for (TaskStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TaskStatus id: " + id);
    }
}
