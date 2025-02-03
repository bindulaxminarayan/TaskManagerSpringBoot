package com.tutorial.service;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import org.apache.coyote.BadRequestException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface TaskService {
    TaskEntity createTask(TaskEntity task) throws BadRequestException;

    TasksResponseDTO findAll();

    TasksResponseDTO getTasks(String status, String priority, String dueDate) throws BadRequestException;

    Optional<TaskEntity> getTaskById(Long taskId);

    TaskEntity updateTask(Long taskId, Map<String, Object> updates) throws BadRequestException;
}
