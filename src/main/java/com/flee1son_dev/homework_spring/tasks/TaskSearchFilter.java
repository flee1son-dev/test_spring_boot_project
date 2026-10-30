package com.flee1son_dev.homework_spring.tasks;

public record TaskSearchFilter(
    Long creatorId,
    Long assignedId,
    Integer pageSize,
    Integer pageNumber
) {
    
}
