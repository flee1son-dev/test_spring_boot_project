package com.flee1son_dev.homework_spring;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import javax.naming.LimitExceededException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public Task getTaskById(Long id){
        TaskEntity taskEntity = repository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        return toDomainTask(taskEntity);
    }

    public List<Task> findAllTasks(){
        List<TaskEntity> allEntities = repository.findAll();
        return allEntities.stream()
                            .map(this::toDomainTask).toList();
    }

    public Task createTask(Task taskToCreate) {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if(taskToCreate.status() != null){
            throw new IllegalArgumentException("Status should be empty");
        }
        var savedTask = new TaskEntity(
            null, 
            taskToCreate.creatorId(), 
            null, 
            TaskStatus.CREATED, 
            taskToCreate.createDateTime(), 
            taskToCreate.deadlineDate(), 
            taskToCreate.priority()
        );
        var savedEntity= repository.save(savedTask);
        return toDomainTask(savedEntity);
    }

    public Task taskUpdate(
        Long id,
        Task taskToUpdate
    ) {
        
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(taskEntity.getStatus() == TaskStatus.DONE){
            throw new IllegalArgumentException("Task status cannot be a Done");
        }

        var updatedTask = new TaskEntity(
            taskEntity.getId(), 
            taskToUpdate.creatorId(), 
            taskEntity.getAssignedUserId(),  
            taskEntity.getStatus(), 
            taskEntity.getCreateDateTime(), 
            taskToUpdate.deadlineDate(), 
            taskToUpdate.priority()
        );
        var savedEntity = repository.save(updatedTask);
        return toDomainTask(savedEntity);
    }

    //? public Task taskUpdateStatus(Long id, TaskStatus status) { 
    //?     if(!taskMap.containsKey(id)){
    //?         throw new NoSuchElementException("Not found task by id = " + id);
    //?     }
    //?     var task = taskMap.get(id);
    //?     if (status == task.status()){
    //?         throw new IllegalArgumentException("The status is already " + status);
    //?     }
    //?     var updatedTask = new Task(
    //?         task.id(), 
    //?         task.creatorId(), 
    //?         task.assignedUserId(), 
    //?         status, 
    //?         task.createDateTime(), 
    //?         task.deadlineDate(), 
    //?         task.priority()
    //?     );
    //?     taskMap.put(id, updatedTask);
    //?     return updatedTask;
    //? }

    public Task taskResumption(Long id) {

        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));

        
        if(taskEntity.getStatus() != TaskStatus.DONE){
            throw new IllegalStateException("The status can only be 'done'");
        }
        var resumptionTask = new TaskEntity(
            taskEntity.getId(), 
            taskEntity.getCreatorId(), 
            taskEntity.getAssignedUserId(), 
            TaskStatus.IN_PROGRESS, 
            taskEntity.getCreateDateTime(), 
            taskEntity.getDeadlineDate(), 
            taskEntity.getPriority()  
        );

        var savedEntity = repository.save(resumptionTask);
        return toDomainTask(savedEntity);
    }

    public void taskDelete(Long id) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        repository.delete(taskEntity);
    }

    private Task toDomainTask(TaskEntity task){
        return new Task(
            task.getId(), 
            task.getCreatorId(), 
            task.getAssignedUserId(), 
            task.getStatus(), 
            task.getCreateDateTime(), 
            task.getDeadlineDate(), 
            task.getPriority()
        );
    }

    public Task taskStart(Long id, Long assignedId) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if (taskEntity.getStatus() != TaskStatus.CREATED){
            throw new IllegalStateException("Incorrect task's status");
        }
        if(assignedId == null){
            throw new IllegalArgumentException("An assigned user is required to start the task.");
        }
        
        if(limitAssignedTasks(id, assignedId)){
            throw new IllegalStateException("Assigned user cannot have too many tasks");
        }

        var started = new TaskEntity(
            taskEntity.getId(), 
            taskEntity.getCreatorId(), 
            assignedId, 
            TaskStatus.IN_PROGRESS, 
            taskEntity.getCreateDateTime(), 
            taskEntity.getDeadlineDate(), 
            taskEntity.getPriority()
        );
        if(started.getAssignedUserId() == null){
            throw new IllegalStateException("Assigned user id cannot be a empty");
        }


        var savedEntity = repository.save(started);
        return toDomainTask(savedEntity);
    }

    private boolean limitAssignedTasks(Long id, Long assignedId){
        var allTasks = repository.findAll();
        int count = 0;

        for(TaskEntity existingTask : allTasks) {
            if (existingTask.getAssignedUserId() == assignedId){
                count++;
            }
        }
        if(count >= 4){
                return true;
            }
        return false;
    }
}
