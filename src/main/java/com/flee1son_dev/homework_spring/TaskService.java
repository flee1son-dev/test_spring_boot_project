package com.flee1son_dev.homework_spring;

import java.time.LocalDate;
import java.util.List;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

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
        if(!taskToCreate.deadlineDate().isAfter(taskToCreate.createDateTime())){
            throw new IllegalArgumentException("Create date must be a 1 day earlier deadline date.");
        }
        var savedTask = new TaskEntity(
            null, 
            taskToCreate.creatorId(), 
            null, 
            TaskStatus.CREATED, 
            taskToCreate.createDateTime(), 
            taskToCreate.deadlineDate(),
            null,
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
        
        if(taskEntity.getStatus().equals(TaskStatus.DONE)){
            throw new IllegalStateException("A completed task cannot be edited.");
        }
        if(taskEntity.getStatus().equals(TaskStatus.IN_PROGRESS)){
            throw new IllegalStateException("A in progress task cannot be edited");
        }
        if(!taskToUpdate.deadlineDate().isAfter(taskEntity.getCreateDateTime())){
            throw new IllegalArgumentException("Create date must be a 1 day earlier deadline date.");
        }

        var updatedTask = new TaskEntity(
            taskEntity.getId(), 
            taskToUpdate.creatorId(), 
            taskEntity.getAssignedUserId(),  
            taskEntity.getStatus(), 
            taskEntity.getCreateDateTime(), 
            taskToUpdate.deadlineDate(),
            taskEntity.getDoneDate(),
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
        
        taskEntity.setDoneDate(null);
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        return toDomainTask(repository.save(taskEntity));
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
            task.getDoneDate(), 
            task.getPriority()
        );
    }

    public Task taskStart(Long id, Long assignedId) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(assignedId == null){
                    throw new IllegalArgumentException("An assigned user is required to start the task.");
                }

        if (taskEntity.getStatus() != TaskStatus.CREATED){
            throw new IllegalStateException("A completed or in progress task cannot be starting");
        }
        
        
        if(limitAssignedTasks(id, assignedId)){
            throw new IllegalStateException("Assigned user cannot have too many tasks");
        }

        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setAssignedUserId(assignedId);


        return toDomainTask(repository.save(taskEntity)) ;
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

    @Transactional 
    public void taskComplete(Long id) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(taskEntity.getStatus().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("Cannot complete created task");
        }
        if(taskEntity.getStatus().equals(TaskStatus.DONE)){
            throw new IllegalStateException("Cannot complete completed task.");
        }
        
        repository.setStatusToComplete(id, TaskStatus.DONE, LocalDate.now());
        log.info("Successfully completed task: id={id}", id);
    }
}
