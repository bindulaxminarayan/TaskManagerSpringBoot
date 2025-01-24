package com.tutorial;

import com.tutorial.repository.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaAuditing
public class TaskManagerApp {

    private TaskRepository taskRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TaskManagerApp.class, args);
    }

}