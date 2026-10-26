package com.flee1son_dev.homework_spring;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service 
public class TaskService {
    private static final HashMap<Long, Task> taskMap = new HashMap<>();
    private AtomicLong idCount = new AtomicLong(0);

    private long idCounter(){
        Long id = idCount.incrementAndGet();
        return id;
    }

    public Task getTaskById(Long id){
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not fount task by id= " + id);
        }
        return taskMap.get(id);
    }

    public List<Task> findAllTasks(){
        return taskMap.values().stream().toList();
    }

    public Task createTask(Task taskToCreate) {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if(taskToCreate.status() != null){
            throw new IllegalArgumentException("Status should be empty");
        }
        var savedTask = new Task(
            idCounter(), 
            taskToCreate.creatorId(), 
            taskToCreate.assignedUserId(), 
            TaskStatus.CREATED, 
            taskToCreate.createDateTime(), 
            taskToCreate.deadlineDate(), 
            taskToCreate.priority()
        );
        taskMap.put(savedTask.id(), savedTask);
        return savedTask;
    }

    public Task taskUpdate(
        Long id,
        Task taskToUpdate
    ) {
        
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = " + id);
        }
        var oldtask = taskMap.get(id);
        if(oldtask.status() == TaskStatus.DONE){
            throw new IllegalArgumentException("Task status cannot be a Done");
        }

        var updatedTask = new Task(
            id, 
            taskToUpdate.creatorId(), 
            taskToUpdate.assignedUserId(), 
            oldtask.status(), 
            oldtask.createDateTime(), 
            taskToUpdate.deadlineDate(), 
            taskToUpdate.priority()
        );
        
        taskMap.put(updatedTask.id(), updatedTask);
        return updatedTask;
    }

    public Task taskUpdateStatus(Long id, TaskStatus status) {
        
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = " + id);
        }
        var task = taskMap.get(id);
        if (status == task.status()){
            throw new IllegalArgumentException("The status is already " + status);
        }
        var updatedTask = new Task(
            task.id(), 
            task.creatorId(), 
            task.assignedUserId(), 
            status, 
            task.createDateTime(), 
            task.deadlineDate(), 
            task.priority()
        );

        taskMap.put(id, updatedTask);
        return updatedTask;
    }

    public Task taskResumption(Long id) {

        if(!taskMap.containsKey(id)) {
            throw new NoSuchElementException("Not found task by id = " + id);

        }
        var task = taskMap.get(id);
        
        if(task.status() != TaskStatus.DONE){
            throw new IllegalStateException("The status can only be 'done'");
        }
        var resumptionTask = new Task(
            id, 
            task.creatorId(), 
            task.assignedUserId(), 
            TaskStatus.IN_PROGRESS, 
            task.createDateTime(), 
            task.deadlineDate(), 
            task.priority()
        );

        taskMap.put(id, resumptionTask);
        return resumptionTask;
    }

    public Task taskDelete(Long id) {
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id = " + id);
        }
        taskMap.remove(id);
        return null;
    }
}
