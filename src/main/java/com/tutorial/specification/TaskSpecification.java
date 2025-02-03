package com.tutorial.specification;

import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification implements Specification<TaskEntity> {
    private final TaskStatus taskStatus;
    private final TaskPriority taskPriority;

    public TaskSpecification(TaskStatus taskStatus, TaskPriority taskPriority) {
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
    }

    @Override
    public Predicate toPredicate(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (taskStatus != null) {
            predicates.add(criteriaBuilder.equal(root.get("taskStatus"), taskStatus));
        }
        if (taskPriority != null) {
            predicates.add(criteriaBuilder.equal(root.get("taskPriority"), taskPriority));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
