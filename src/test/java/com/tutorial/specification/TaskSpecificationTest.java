package com.tutorial.specification;

import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskSpecificationTest {

    @InjectMocks
    private TaskSpecification taskSpecification;

    @Mock
    private Logger logger; // Mock the logger

    @Mock
    private Root<TaskEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Test
    void testToPredicate_taskStatusNotNull() {

        taskSpecification = new TaskSpecification(TaskStatus.NEW, null, null, null);

        Predicate expectedPredicate = mock(Predicate.class);
        when(criteriaBuilder.equal(root.get("taskStatus"), TaskStatus.NEW)).thenReturn(expectedPredicate);

        // *** KEY CHANGE: Mock the and() call to return the single predicate
        when(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate); // Correct way to handle single predicate

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("taskStatus"), TaskStatus.NEW);
        verify(criteriaBuilder).and(expectedPredicate); // Verify that and was called with the predicate
        assertEquals(expectedPredicate, result);
    }
    @Test
    void testToPredicate_allNull() {
        taskSpecification = new TaskSpecification(null, null, null, null);
        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        // Verify that criteriaBuilder.and() was called with an empty array
        verify(criteriaBuilder).and(new Predicate[0]); // Or an empty List if you prefer
        // Since no predicates are added, and() returns null. We expect null.
        assertEquals(null, result);
    }



    @Test
    void testToPredicate_DueDateEqualTo_multipleCriteria() {
        TaskStatus taskStatus = TaskStatus.BLOCKED;
        TaskPriority taskPriority = TaskPriority.LOW;
        LocalDate dueDate = LocalDate.now();
        String dueDateOperator = "="; // Or any valid operator you want to test

        taskSpecification = new TaskSpecification(taskStatus, taskPriority, dueDate, dueDateOperator);

        Predicate statusPredicate = mock(Predicate.class);
        Predicate priorityPredicate = mock(Predicate.class);
        Predicate datePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("taskStatus"), taskStatus)).thenReturn(statusPredicate);
        when(criteriaBuilder.equal(root.get("taskPriority"), taskPriority)).thenReturn(priorityPredicate);
        when(criteriaBuilder.equal(root.get("dueDate"), dueDate)).thenReturn(datePredicate); // Mock equal for the null operator case
        when(criteriaBuilder.and(statusPredicate, priorityPredicate, datePredicate)).thenReturn(combinedPredicate);

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("taskStatus"), taskStatus);
        verify(criteriaBuilder).equal(root.get("taskPriority"), taskPriority);
        verify(criteriaBuilder).equal(root.get("dueDate"), dueDate); // Verify equal was called
        verify(criteriaBuilder).and(statusPredicate, priorityPredicate, datePredicate);

        assertEquals(combinedPredicate, result);
    }
    @Test
    void testToPredicate_dueDateGreaterThan() {
        LocalDate dueDate = LocalDate.now();
        String dueDateOperator = ">";
        taskSpecification = new TaskSpecification(null, null, dueDate, dueDateOperator);

        Predicate expectedPredicate = mock(Predicate.class);
        when(criteriaBuilder.greaterThan(root.get("dueDate"), dueDate)).thenReturn(expectedPredicate);

        // *** KEY CHANGE: Mock the and() call to return the single predicate
        when(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate); // Correct way to handle single predicate

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).greaterThan(root.get("dueDate"), dueDate);
        verify(criteriaBuilder).and(expectedPredicate); // Verify that and was called with the predicate
        assertEquals(expectedPredicate, result);
    }

    @Test
    void testToPredicate_dueDateGreaterThanEquals() {
        LocalDate dueDate = LocalDate.now();
        String dueDateOperator = ">=";
        taskSpecification = new TaskSpecification(null, null, dueDate, dueDateOperator);

        Predicate expectedPredicate = mock(Predicate.class);
        when(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDate)).thenReturn(expectedPredicate);

        // *** KEY CHANGE: Mock the and() call to return the single predicate
        when(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate); // Correct way to handle single predicate

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).greaterThanOrEqualTo(root.get("dueDate"), dueDate);
        verify(criteriaBuilder).and(expectedPredicate); // Verify that and was called with the predicate
        assertEquals(expectedPredicate, result);
    }

    @Test
    void testToPredicate_dueDateLessThan() {
        LocalDate dueDate = LocalDate.now();
        String dueDateOperator = "<";
        taskSpecification = new TaskSpecification(null, null, dueDate, dueDateOperator);

        Predicate expectedPredicate = mock(Predicate.class);
        when(criteriaBuilder.lessThan(root.get("dueDate"), dueDate)).thenReturn(expectedPredicate);

        // *** KEY CHANGE: Mock the and() call to return the single predicate
        when(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate); // Correct way to handle single predicate

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).lessThan(root.get("dueDate"), dueDate);
        verify(criteriaBuilder).and(expectedPredicate); // Verify that and was called with the predicate
        assertEquals(expectedPredicate, result);
    }

    @Test
    void testToPredicate_dueDateLessThanEquals() {
        LocalDate dueDate = LocalDate.now();
        String dueDateOperator = "<=";
        taskSpecification = new TaskSpecification(null, null, dueDate, dueDateOperator);

        Predicate expectedPredicate = mock(Predicate.class);
        when(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), dueDate)).thenReturn(expectedPredicate);

        // *** KEY CHANGE: Mock the and() call to return the single predicate
        when(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate); // Correct way to handle single predicate

        Predicate result = taskSpecification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).lessThanOrEqualTo(root.get("dueDate"), dueDate);
        verify(criteriaBuilder).and(expectedPredicate); // Verify that and was called with the predicate
        assertEquals(expectedPredicate, result);
    }
}