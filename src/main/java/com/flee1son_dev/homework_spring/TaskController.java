package com.flee1son_dev.homework_spring;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController 
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }
    
    @GetMapping
    public String Health() {
        return "App is running";
    }
    
    @GetMapping("/tasks/{id}")
    public Task getaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.findAllTasks();
    }
}
