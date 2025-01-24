package com.tutorial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.entity.converter.TaskPriorityConverter;
import com.tutorial.entity.converter.TaskStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_entity_seq")
    @SequenceGenerator(name = "my_entity_seq", sequenceName = "my_entity_seq", allocationSize = 1)
    private Long taskId;

    @Size(max=100, message = "Cannot exceed 100 characters")
    @JsonProperty("summary")
    @NotBlank(message = "Task summary is required")
    private String taskSummary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false, name="created_at")
    @CreatedDate
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    @LastModifiedDate
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Task status id cannot be empty")
    @Convert(converter = TaskStatusConverter.class)
    @Column(name = "task_status_id", nullable = false)
    @JsonProperty("status")
    private TaskStatus taskStatus = TaskStatus.NEW;

    @NotNull(message = "Task priority id cannot be empty")
    @Convert(converter = TaskPriorityConverter.class)
    @Column(name = "task_priority_id", nullable = false)
    @JsonProperty("priority")
    private TaskPriority taskPriority = TaskPriority.UNSPECIFIED;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("notes")
    private String taskNotes;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Column(columnDefinition = "DATE")
    @JsonProperty("due_date")
    private LocalDate dueDate = LocalDate.now().plus(1, ChronoUnit.MONTHS);
    public TaskEntity() {
    }

    @Override
    public String toString() {
        return "Tasks{" + "Tasks Summary=" + taskSummary + '}';
    }

    public Long getId() {
        return taskId;
    }

    public void setId(Long id) {
        this.taskId = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskSummary() {
        return taskSummary;
    }

    public void setTaskSummary(String taskSummary) {
        this.taskSummary = taskSummary;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }
}
