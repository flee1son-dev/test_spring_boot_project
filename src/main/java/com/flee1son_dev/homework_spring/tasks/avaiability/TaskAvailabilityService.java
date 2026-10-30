package com.flee1son_dev.homework_spring.tasks.avaiability;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flee1son_dev.homework_spring.tasks.TaskEntity;
import com.flee1son_dev.homework_spring.tasks.TaskRepository;
import com.flee1son_dev.homework_spring.tasks.TaskStatus;

@Service 
public class TaskAvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(TaskAvailabilityService.class);

    private final TaskRepository repository;

    public TaskAvailabilityService(TaskRepository repository){
        this.repository = repository;
    }


    public boolean isTaskAvailable(
        Long id,
        Long assignedId
    ){
        List<Long> tasksLimitAssignedIds = repository.findLimitedAssignedUserIds(
            id, 
            assignedId, 
            TaskStatus.IN_PROGRESS
        );
        if(tasksLimitAssignedIds.size() <= 4){
            return true;
        }

        log.info("Conflict: the assigned user has too many tasks in progress.");
        return false;
    }
}
