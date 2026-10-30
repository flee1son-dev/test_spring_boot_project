package com.flee1son_dev.homework_spring.tasks;

import java.time.LocalDate;
import java.util.List;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flee1son_dev.homework_spring.tasks.avaiability.TaskAvailabilityService;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class TaskService {

    private final TaskController taskController;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskAvailabilityService availabilityService;

    private final TaskMapper mapper;

    private final TaskRepository repository;

    public TaskService(
        TaskRepository repository, 
        TaskMapper mapper, 
        TaskController taskController,
        TaskAvailabilityService avaiabilityService
    ){
        this.repository = repository;
        this.mapper = mapper;
        this.taskController = taskController;
        this.availabilityService = avaiabilityService;
    }

    public Task getTaskById(Long id){
        TaskEntity taskEntity = repository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        return mapper.toDomain(taskEntity);
    }

    public List<Task> searchAllTasksByFilter(
        TaskSearchFilter filter
    ){
        int pageSize = filter.pageSize() != null 
                ? filter.pageSize() : 10;

        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0;

        Pageable pageable = Pageable
                                .ofSize(pageSize)
                                .withPage(pageNumber);
        
        List<TaskEntity> allEntities = repository.searchAllByFilter(
            filter.creatorId(),
            filter.assignedId(),
            pageable
        );

        return allEntities.stream()
                            .map(mapper::toDomain).toList();
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
        return mapper.toDomain(savedEntity);
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
        return mapper.toDomain(savedEntity);
    }

    @Transactional 
    public Task taskResumption(Long id) {

        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));

        
        if(taskEntity.getStatus() != TaskStatus.DONE){
            throw new IllegalStateException("The status can only be 'done'");
        }
        
        taskEntity.setDoneDate(null);
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        var savedEntity = repository.save(taskEntity);
        return mapper.toDomain(savedEntity);
    }


    public void taskDelete(Long id) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(!taskEntity.getStatus().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("A task that has already been accepted or completed cannot be canceled.");
        }
        taskEntity.setStatus(TaskStatus.CANCELLED);
        var savedEntity = repository.save(taskEntity);
        log.info("Successfully cancelled task: id={}", id);

    }

    
    @Transactional
    public Task taskStart(Long id, Long assignedId) {
        var taskEntity = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(assignedId == null){
                    throw new IllegalArgumentException("An assigned user is required to start the task.");
                }

        if (taskEntity.getStatus() != TaskStatus.CREATED){
            throw new IllegalStateException("A completed or in progress task cannot be starting");
        }
        
        var isAvailableToGet = availabilityService.isTaskAvailable(id, assignedId);
        if(!isAvailableToGet){
            throw new IllegalStateException("Assigned user cannot have too many tasks");
        }

        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setAssignedUserId(assignedId);

        var savedEntity = repository.save(taskEntity);

        return mapper.toDomain(savedEntity) ;
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
