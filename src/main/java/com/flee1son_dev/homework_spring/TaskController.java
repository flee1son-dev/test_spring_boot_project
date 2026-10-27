package com.flee1son_dev.homework_spring;

import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
        log.info("Called get task by id: id = " + id);
        try{
            return ResponseEntity.ok(taskService.getTaskById(id));
        } catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Called get all tasks");
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
        @RequestBody Task taskCreate
    ){
        log.info("Called create tasks");
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskCreate));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
        @PathVariable("id") long id,
        @RequestBody Task taskToUpdate
        ) {
        log.info("Called update task by id = " + id);
        try{
            var updated = taskService.taskUpdate(id, taskToUpdate);
            return ResponseEntity.ok(updated);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
    }

    // @PutMapping("/{id}/status")
    // public Task updateTaskStatus(
    //     @PathVariable("id") Long id, 
    //     @RequestBody TaskStatus status
    // ) {
    //     log.info("Called update task's status by id = " + id);
    //     return taskService.taskUpdateStatus(id, status);
    // }

    @PutMapping("/{id}/resumption")
    public ResponseEntity<Task> resumptionTask(
        @PathVariable("id") Long id
    ) {
            log.info("Called resumption task by id = " + id);
            try{
                var resumption = taskService.taskResumption(id);
                return ResponseEntity.ok(resumption);
            } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTask(
        @PathVariable("id") Long id
    ) {
        try{
            taskService.taskDelete(id);
            return ResponseEntity.ok().build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Task> startTask(
        @PathVariable("id") Long id,
        @RequestBody(required = false) Long assignedId
    ) {
        try{
            var started = taskService.taskStart(id, assignedId);
            return ResponseEntity.ok(started);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
