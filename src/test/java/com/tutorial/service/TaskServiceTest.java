package com.tutorial.service;

import com.tutorial.dto.TasksResponseDTO;
import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import com.tutorial.impl.TaskServiceImpl;
import com.tutorial.repository.TaskRepository;
import com.tutorial.specification.TaskSpecification;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskService;
    @InjectMocks
    private TaskSpecification taskSpecification;

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);

    @BeforeEach
    public void setUp() {
        // Initialize the StatusEntity object to be used in tests
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTaskWithoutNotes() throws BadRequestException {
        TaskEntity taskEntity = new TaskEntity();
        LocalDate date = LocalDate.now();
        LocalDateTime taskDate = LocalDateTime.now();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        taskEntity.setTaskStatus(TaskStatus.NEW);
        taskEntity.setCreatedAt(taskDate);
        taskEntity.setUpdatedAt(taskDate);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getTaskStatus().name(), "NEW");
        assertEquals(taskEntity.getCreatedAt(), resultEntity.getCreatedAt());
        assertEquals(taskEntity.getUpdatedAt(), resultEntity.getUpdatedAt());
        assertEquals(LocalDate.now().plus(1, ChronoUnit.MONTHS), resultEntity.getDueDate());

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    public void createTaskWithNotes() throws BadRequestException {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setTaskNotes("Task1 to work");
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getTaskStatus().name(), "IN_PROGRESS");
        assertEquals(resultEntity.getTaskNotes(), taskEntity.getTaskNotes());

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    public void createTaskWithNoStatus() throws BadRequestException {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getTaskStatus().name(), "NEW");
        assertEquals(resultEntity.getTaskNotes(), taskEntity.getTaskNotes());

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    public void createTaskWithNoPriority() throws BadRequestException {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getTaskStatus().name(), "NEW");
        assertEquals(resultEntity.getTaskNotes(), taskEntity.getTaskNotes());
        assertEquals(resultEntity.getTaskPriority(), TaskPriority.UNSPECIFIED);

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    public void createTaskWithStatusAndPriority() throws BadRequestException {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setTaskPriority(TaskPriority.LOW);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getTaskStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(resultEntity.getTaskNotes(), taskEntity.getTaskNotes());
        assertEquals(resultEntity.getTaskPriority(), TaskPriority.LOW);

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    public void createTaskWithDueDate() throws BadRequestException {
        LocalDate date = LocalDate.now();
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskSummary("t1");
        taskEntity.setDueDate(date);

        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        TaskEntity resultEntity = taskService.createTask(taskEntity);

        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), taskEntity.getId());
        assertEquals(resultEntity.getTaskSummary(), taskEntity.getTaskSummary());
        assertEquals(resultEntity.getDueDate(), taskEntity.getDueDate());

        // Verify that repository method was called once
        verify(taskRepository, times(1)).save(taskEntity);
    }


    @Test
    public void findAllTestViaFetchTasks() throws BadRequestException {
        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setId(1L);
        taskEntity1.setTaskSummary("t1");
        taskEntity1.setTaskStatus(TaskStatus.NEW);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setId(1L);
        taskEntity2.setTaskSummary("t2");
        taskEntity2.setTaskStatus(TaskStatus.IN_PROGRESS);

        List<TaskEntity> mockTasks = Arrays.asList(taskEntity1, taskEntity2);
        when(taskRepository.findAll(any(TaskSpecification.class))).thenReturn(mockTasks);

        TasksResponseDTO result = taskService.getTasks(null, null,null);
        assertNotNull(result);
        assertEquals(2, result.getTasks().size()); // Verify size of returned list
        assertEquals("t1", result.getTasks().get(0).getTaskSummary()); // Verify first task name
        assertEquals("t2", result.getTasks().get(1).getTaskSummary()); // Verify second task name

        // Verify repository method was called exactly once
        verify(taskRepository, times(1)).findAll(any(TaskSpecification.class));
    }

    @Test
    public void getTaskByStatus() throws BadRequestException {
        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setId(1L);
        taskEntity1.setTaskSummary("t1");
        taskEntity1.setTaskStatus(TaskStatus.NEW);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setId(1L);
        taskEntity2.setTaskSummary("t2");
        taskEntity2.setTaskStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findAll(any(TaskSpecification.class))).thenReturn(Arrays.asList(taskEntity2));
        TasksResponseDTO result = taskService.getTasks("IN_PROGRESS", null,null);
        assertNotNull(result);
        assertEquals(1, result.getTasks().size()); // Verify size of returned list
        assertEquals("t2", result.getTasks().get(0).getTaskSummary()); // Verify second task name

        // Verify repository method was called exactly once
        verify(taskRepository, times(1)).findAll(any(TaskSpecification.class));
    }

    @Test
    public void getTaskByPriority() throws BadRequestException {
        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setId(1L);
        taskEntity1.setTaskSummary("t1");
        taskEntity1.setTaskPriority(TaskPriority.LOW);

        TaskEntity taskEntity2 = new TaskEntity();
        taskEntity2.setId(1L);
        taskEntity2.setTaskSummary("t2");
        taskEntity2.setTaskPriority(TaskPriority.CRITICAL);

        when(taskRepository.findAll(any(TaskSpecification.class))).thenReturn(Arrays.asList(taskEntity2));
        TasksResponseDTO result = taskService.getTasks(null, "CRITICAL",null);
        assertNotNull(result);
        assertEquals(1, result.getTasks().size()); // Verify size of returned list
        assertEquals("t2", result.getTasks().get(0).getTaskSummary()); // Verify second task name

        // Verify repository method was called exactly once
        verify(taskRepository, times(1)).findAll(any(TaskSpecification.class));
    }

    @Test
    public void getTaskByDueDate() throws BadRequestException {
        LocalDate dueDate = LocalDate.now();
        TaskEntity taskEntity1 = new TaskEntity();
        taskEntity1.setId(1L);
        taskEntity1.setTaskSummary("t1");
        taskEntity1.setDueDate(dueDate);
       //Mock Task Repository
        TaskSpecification specification = new TaskSpecification(null, null, dueDate,null);
        when(taskRepository.findAll(any(specification.getClass()))).thenReturn(Arrays.asList(taskEntity1));

        //Validate the taskservice
        TasksResponseDTO result = taskService.getTasks(null, null,dueDate.toString());
        assertNotNull(result);
        assertEquals(1, result.getTasks().size()); // Verify size of returned list
        assertEquals("t1", result.getTasks().get(0).getTaskSummary());
        assertEquals(dueDate,result.getTasks().get(0).getDueDate());

        // Verify repository method was called exactly once
        verify(taskRepository, times(1)).findAll(any(specification.getClass()));
    }

    @Test
    public void getTaskById() {
        TaskEntity t1 = new TaskEntity();
        t1.setTaskSummary("t1");
        t1.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t1));

        Optional<TaskEntity> t2 = taskService.getTaskById(1L);
        assertNotNull(t2.get());
        assertEquals(t2.get(), t1); // Verify size of returned list
        // Verify repository method was called exactly once
        verify(taskRepository, times(1)).findById(1L);
    }
}
