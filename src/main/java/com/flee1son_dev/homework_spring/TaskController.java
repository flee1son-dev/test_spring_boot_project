package com.flee1son_dev.homework_spring;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }
    
    @GetMapping("/{id}")
    public Task getaskById(@PathVariable("id") Long id) {
        log.info("Called get task by id: id = " + id);
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Called get all tasks");
        return taskService.findAllTasks();
    }

    @PostMapping
    public Task createTask(
        @RequestBody Task taskCreate
    ){
        log.info("Called create tasks");
        return taskService.createTask(taskCreate);
    }
    
    @PutMapping("/{id}")
    public Task updateTask(
        @PathVariable("id") long id,
        @RequestBody Task taskToUpdate
        ) {
        log.info("Called update task by id = " + id);
        return taskService.taskUpdate(id, taskToUpdate);
    }

    @PutMapping("/{id}/status")
    public Task updateTaskStatus(
        @PathVariable("id") Long id, 
        @RequestBody TaskStatus status
    ) {
        log.info("Called update task's status by id = " + id);
        return taskService.taskUpdateStatus(id, status);
    }

    @PutMapping("/{id}/resumption")
    public Task resumptionTask(
        @PathVariable("id") Long id
    ) {
            log.info("Called resumption task by id = " + id);
            return taskService.taskResumption(id);         
    }

    @DeleteMapping("/{id}/delete")
    public void deleteTask(
        @PathVariable("id") Long id
    ) {
        taskService.taskDelete(id);
    }
}
