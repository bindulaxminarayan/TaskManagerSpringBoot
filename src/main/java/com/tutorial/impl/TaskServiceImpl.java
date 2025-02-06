package com.tutorial.impl;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import com.tutorial.repository.TaskRepository;
import com.tutorial.service.TaskService;
import com.tutorial.specification.TaskSpecification;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private TaskRepository taskRepository;

    @Autowired
    public void TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    @Override
    /**
     * Create a new task
     *
     * @param task The task to create
     * @return The created task
     */
    public TaskEntity createTask(TaskEntity task) throws BadRequestException {
        logger.debug("Task details before save: {}", task);
        TaskEntity createdTask = taskRepository.save(task);
        logger.info("Task successfully created with ID: {}", createdTask);
        return createdTask;
    }

    @Override
    public TasksResponseDTO getTasks(String status, String priority, String dueDateStr) throws BadRequestException {
        TasksResponseDTO tasksResponse = new TasksResponseDTO();

        // Parse the status and priority inputs into enums
        TaskStatus taskStatus = parseEnumValue(status, TaskStatus.class, "Invalid task status: " + status);
        TaskPriority taskPriority = parseEnumValue(priority, TaskPriority.class, "Invalid task priority: " + priority);
        //Convert dueDate from String to Date
        String dueDateOperator = "="; // Default is equality
        LocalDate dueDate = null;

        if (dueDateStr != null && !dueDateStr.isBlank()) {
            if (dueDateStr.startsWith(">=") || dueDateStr.startsWith("<=") ||
                    dueDateStr.startsWith(">") || dueDateStr.startsWith("<")) {
                logger.debug("Due date Operator: "+dueDateOperator);
                dueDateOperator = dueDateStr.substring(0, dueDateStr.contains("=") ? 2 : 1); // Extract operator
                dueDate = LocalDate.parse(dueDateStr.substring(dueDateOperator.length())); // Extract date
            } else {
                dueDate = LocalDate.parse(dueDateStr); // Default case (equal)
            }
        }
        //Fetch tasks
        List<TaskEntity> tasks = taskRepository.findAll(new TaskSpecification(taskStatus, taskPriority, dueDate, dueDateOperator));

        // Prepare the response
        tasksResponse.setTasks(tasks);
        tasksResponse.setTotalCount(tasks.size());
        return tasksResponse;
    }

    private <E extends Enum<E>> E parseEnumValue(String value, Class<E> enumType, String errorMessage) throws BadRequestException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            logger.info("Enum value:" + Enum.valueOf(enumType, value.toUpperCase()));
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    public Optional<TaskEntity> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public TaskEntity updateTask(Long id, Map<String, Object> updates) throws BadRequestException {
        // Retrieve the existing task
        TaskEntity task = taskRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Task not found with ID: " + id));

        // Update fields if present
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "summary":
                    task.setTaskSummary((String) value);
                    break;
                case "due_date":
                    if (value != null) {
                        try {
                            task.setDueDate(LocalDate.parse((String) value));
                        } catch (DateTimeParseException ex) {
                            throw new BadRequestException("Invalid date format for due_date. Expected format: yyyy-MM-dd");
                        }
                    } else {
                        task.setDueDate(null);
                    }
                    break;
                case "notes":
                    task.setTaskNotes((String) value);
                    break;
                case "priority":
                    if (value != null) {
                        try {
                            task.setTaskPriority(TaskPriority.valueOf(((String) value).toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            throw new BadRequestException("Invalid priority value: " + value);
                        }
                    }
                    break;
                case "status":
                    if (value != null) {
                        try {
                            task.setTaskStatus(TaskStatus.valueOf(((String) value).toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            throw new BadRequestException("Invalid status value: " + value);
                        }
                    }
                    break;
                default:
                    throw new BadRequestException("Unknown field: " + key);
            }
        }

        // Update the timestamp and save
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

}
