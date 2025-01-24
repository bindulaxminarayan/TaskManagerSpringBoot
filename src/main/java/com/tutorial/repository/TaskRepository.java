package com.tutorial.repository;

import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByTaskStatus(TaskStatus taskStatus);

    List<TaskEntity> findByTaskPriority(TaskPriority taskPriority);

    List<TaskEntity> findByTaskStatusAndTaskPriority(TaskStatus taskStatus, TaskPriority priority);
}
