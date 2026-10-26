package com.flee1son_dev.homework_spring;

import java.time.LocalDateTime;

public record Task(
    Long id,
    Long creatorId,
    Long assignedUserId,
    TaskStatus status,
    LocalDateTime createDateTime,
    LocalDateTime deadlineDate,
    TaskPriority priority
) {

}
