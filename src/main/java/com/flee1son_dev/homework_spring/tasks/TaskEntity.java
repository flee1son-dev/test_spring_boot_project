package com.flee1son_dev.homework_spring.tasks;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "tasks")
public class TaskEntity {
    @Id 
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "assigned_user_id")
    Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) 
    TaskStatus status;

    @Column(name = "create_date", nullable = false)
    LocalDate createDateTime;

    @Column(name = "deadline_date", nullable = false)
    LocalDate deadlineDate;

    @Column (name = "done_date")
    LocalDate doneDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    TaskPriority priority;

    public TaskEntity(){

    }

    public TaskEntity(Long id, Long creatorId, Long assignedUserId, TaskStatus status, LocalDate createDateTime,
            LocalDate deadlineDate, LocalDate doneDate, TaskPriority priority) {
        this.id = id;
        this.creatorId = creatorId;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.createDateTime = createDateTime;
        this.deadlineDate = deadlineDate;
        this.doneDate = doneDate;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDate createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public LocalDate getDoneDate(){
        return doneDate;
    }

    public void setDoneDate(LocalDate doneDate){
        this.doneDate = doneDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    
}
