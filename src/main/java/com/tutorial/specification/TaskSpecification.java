package com.tutorial.specification;

import com.tutorial.entity.TaskEntity;
import com.tutorial.entity.TaskPriority;
import com.tutorial.entity.TaskStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification implements Specification<TaskEntity> {
    private final TaskStatus taskStatus;
    private final TaskPriority taskPriority;
    private final LocalDate dueDate;
    private final String dueDateOperator;

    private final Logger logger = LoggerFactory.getLogger(TaskSpecification.class);


    public TaskSpecification(TaskStatus taskStatus, TaskPriority taskPriority, LocalDate dueDate, String dueDateOperator) {
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.dueDateOperator = dueDateOperator;
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
        if (dueDate != null) {
            switch (dueDateOperator) {
                case ">":
                    predicates.add(criteriaBuilder.greaterThan(root.get("dueDate"), dueDate));
                    break;
                case ">=":
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDate));
                    break;
                case "<":
                    predicates.add(criteriaBuilder.lessThan(root.get("dueDate"), dueDate));
                    break;
                case "<=":
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), dueDate));
                    break;
                default:
                    predicates.add(criteriaBuilder.equal(root.get("dueDate"), dueDate)); // Default to `=`
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
