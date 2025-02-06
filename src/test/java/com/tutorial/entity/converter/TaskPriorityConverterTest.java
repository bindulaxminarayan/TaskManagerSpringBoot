package com.tutorial.entity.converter;

import com.tutorial.entity.TaskPriority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskPriorityConverterTest {

    @Autowired
    private TaskPriorityConverter taskPriorityConverter = new TaskPriorityConverter();

    @Test
    public void convertToDatabaseColumnTest(){
        TaskPriority input = TaskPriority.CRITICAL;
        Integer expectedOutput = 1;
        assertEquals (expectedOutput, taskPriorityConverter.convertToDatabaseColumn(input));
    }

    @Test
    public void convertToDatabaseColumnTestNull(){
        assertNull(taskPriorityConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void convertToEntityAttribute(){
        TaskPriority expectedOutput = TaskPriority.CRITICAL;
        Integer input = 1;
        assertEquals (expectedOutput, taskPriorityConverter.convertToEntityAttribute(input));
    }

    @Test
    public void convertToEntityAttributeTestNull(){
        assertNull(taskPriorityConverter.convertToEntityAttribute(null));
    }
}
