package com.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.entity.TaskEntity;

import java.util.List;

public class TasksResponseDTO {
    @JsonProperty("total_count")
    private int totalCount;
    private List<TaskEntity> tasks;

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int total_count) {
        this.totalCount = total_count;
    }


}
