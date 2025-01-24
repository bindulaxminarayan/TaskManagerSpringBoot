package com.tutorial.entity.converter;

import com.tutorial.entity.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskStatus taskStatus) {
        return taskStatus != null ? taskStatus.getId() : null;
    }

    @Override
    public TaskStatus convertToEntityAttribute(Integer id) {
        return id != null ? TaskStatus.fromId(id) : null;
    }
}

