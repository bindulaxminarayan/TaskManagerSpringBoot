package com.tutorial.impl;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import com.tutorial.repository.TaskRepository;
import com.tutorial.service.TaskService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public TasksResponseDTO findAll() {
        TasksResponseDTO tasksResponse = new TasksResponseDTO();
        List<TaskEntity> tasks = taskRepository.findAll();
        tasksResponse.setTasks(tasks);
        tasksResponse.setTotalCount(tasks.size());
        return tasksResponse;
    }

    @Override
    public TasksResponseDTO getTasks(String status, String priority) throws BadRequestException {
        TasksResponseDTO tasksResponse = new TasksResponseDTO();

        // Parse the status and priority inputs into enums
        TaskStatus taskStatus = parseEnumValue(status, TaskStatus.class, "Invalid task status: " + status);
        TaskPriority taskPriority = parseEnumValue(priority, TaskPriority.class, "Invalid task priority: " + priority);

        // Fetch tasks dynamically based on filters
        List<TaskEntity> tasks = fetchTasks(taskStatus, taskPriority);

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
            logger.info("Enum value:"+Enum.valueOf(enumType, value.toUpperCase()));
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(errorMessage);
        }
    }

    private List<TaskEntity> fetchTasks(TaskStatus taskStatus, TaskPriority taskPriority) {
        if (taskStatus != null && taskPriority != null) {
            return taskRepository.findByTaskStatusAndTaskPriority(taskStatus, taskPriority);
        } else if (taskStatus != null) {
            logger.info("Task status"+taskStatus);
            return taskRepository.findByTaskStatus(taskStatus);
        } else if (taskPriority != null) {
            logger.info("Task priority"+taskPriority);
            return taskRepository.findByTaskPriority(taskPriority);
        } else {
            return taskRepository.findAll();
        }
    }

    @Override
    public Optional<TaskEntity> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
}
