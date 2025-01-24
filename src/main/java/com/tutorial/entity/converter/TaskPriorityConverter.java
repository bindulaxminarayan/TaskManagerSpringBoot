package com.tutorial.entity.converter;

import com.tutorial.entity.TaskPriority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskPriorityConverter implements AttributeConverter<TaskPriority, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TaskPriority taskPriority) {
        return taskPriority != null ? taskPriority.getId() : null;
    }

    @Override
    public TaskPriority convertToEntityAttribute(Integer id) {
        return id!=null ? TaskPriority.fromId(id) : null;
    }
}
