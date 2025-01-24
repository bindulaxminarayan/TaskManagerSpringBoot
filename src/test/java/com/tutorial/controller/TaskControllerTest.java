package com.tutorial.controller;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskStatus;
import com.tutorial.repository.TaskRepository;
import com.tutorial.service.TaskService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTasks() throws BadRequestException {
        TaskEntity t1 = new TaskEntity();
        t1.setTaskSummary("T1");
        t1.setId(1L);
        TaskEntity t2 = new TaskEntity();
        t2.setTaskSummary("T2");
        t2.setId(2L);
        List<TaskEntity> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        TasksResponseDTO taskResponse = new TasksResponseDTO();
        taskResponse.setTasks(tasks);
        taskResponse.setTotalCount(2);

        when(taskService.getTasks(null,null)).thenReturn(taskResponse);
        TasksResponseDTO expectedTasks = taskController.getTasks(null,null);
        assertEquals(expectedTasks, taskResponse);
        assertEquals(2, taskResponse.getTotalCount());
        verify(taskService, times(1)).getTasks(null,null);
    }

    @Test
    public void testGetTasksStatus() throws BadRequestException {
        TaskEntity t1 = new TaskEntity();
        t1.setTaskSummary("T1");
        t1.setId(1L);
        t1.setTaskStatus(TaskStatus.IN_PROGRESS);
        TaskEntity t2 = new TaskEntity();
        t2.setTaskSummary("T2");
        t2.setId(2L);
        List<TaskEntity> tasks = new ArrayList<>();
        tasks.add(t1);
        TasksResponseDTO taskResponse = new TasksResponseDTO();
        taskResponse.setTasks(tasks);
        taskResponse.setTotalCount(1);

        when(taskService.getTasks("IN_PROGRESS",null)).thenReturn(taskResponse);
        TasksResponseDTO expectedTasks = taskController.getTasks("IN_PROGRESS",null);
        assertEquals(expectedTasks, taskResponse);
        assertEquals(1, taskResponse.getTotalCount());
        verify(taskService, times(1)).getTasks("IN_PROGRESS",null);
    }

    @Test
    public void testGetValidTaskById() throws BadRequestException {
        TaskEntity t1 = new TaskEntity();
        t1.setTaskSummary("T1");
        t1.setId(1L);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(t1));
        TaskEntity t2 = taskController.getTaskById(1L);
        assertEquals(t2, t1);
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testGetInValidTaskById() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            taskController.getTaskById(1L);
        });
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testValidCreateTask() throws BadRequestException {
        TaskEntity t1 = new TaskEntity();
        t1.setTaskSummary("t1");
        t1.setId(1L);
        t1.setTaskStatus(TaskStatus.NEW);
        when(taskService.createTask(t1)).thenReturn(t1);
        TaskEntity t2 = taskController.createTask(t1);
        assertEquals(t2, t1);
        verify(taskService, times(1)).createTask(t1);
    }

}
