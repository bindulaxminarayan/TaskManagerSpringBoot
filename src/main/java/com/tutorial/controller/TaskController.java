package com.tutorial.controller;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskStatus;
import com.tutorial.service.TaskService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    public TaskController(TaskService tService) {
        this.taskService = tService;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskEntity createTask(@Valid @RequestBody TaskEntity taskEntity) throws BadRequestException {
        taskService.createTask(taskEntity);
        return taskEntity;
    }

    @GetMapping("/tasks")
    public TasksResponseDTO getTasks(
            @RequestParam(value="status", required = false) String status,
            @RequestParam(value="priority", required = false) String priority
    ) throws BadRequestException {
        return taskService.getTasks(status, priority);
    }

    @GetMapping("/tasks/{id}")
    public TaskEntity getTaskById(@Valid @PathVariable(name = "id") Long id) throws BadRequestException {
        return taskService.getTaskById(id).orElseThrow(() -> new BadRequestException("Task with id:" + id + "does not exist"));
    }
}
