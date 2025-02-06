package com.tutorial.entity.converter;

import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskStatusConverterTest {
    @Autowired
    private TaskStatusConverter taskStatusConverter = new TaskStatusConverter();

    @Test
    public void convertToDatabaseColumnTest(){
        TaskStatus input = TaskStatus.COMPLETED;
        Integer expectedOutput = 3;
        assertEquals (expectedOutput, taskStatusConverter.convertToDatabaseColumn(input));
    }

    @Test
    public void convertToDatabaseColumnTestNull(){
        assertNull(taskStatusConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void convertToEntityAttribute(){
        TaskStatus expectedOutput = TaskStatus.DEFERRED;
        Integer input = 5;
        assertEquals (expectedOutput, taskStatusConverter.convertToEntityAttribute(input));
    }

    @Test
    public void convertToEntityAttributeTestNull(){
        assertNull(taskStatusConverter.convertToEntityAttribute(null));
    }
}
