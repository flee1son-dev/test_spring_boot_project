package com.flee1son_dev.homework_spring.tasks;

import org.springframework.stereotype.Component;

@Component 
public class TaskMapper {

    public Task toDomain(TaskEntity task){
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
        public TaskEntity toDomain(Task task){
        return new TaskEntity(
            task.id(), 
            task.creatorId(), 
            task.assignedUserId(), 
            task.status(), 
            task.createDateTime(), 
            task.deadlineDate(),
            task.doneDate(), 
            task.priority()
        );
    }
}
