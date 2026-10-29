package com.flee1son_dev.homework_spring;

import java.time.LocalDate;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record Task(
    @Null 
    Long id,

    @NotNull 
    Long creatorId,

    Long assignedUserId,

    TaskStatus status,

    @NotNull @FutureOrPresent 
    LocalDate createDateTime,

    @NotNull @Future 
    LocalDate deadlineDate,

    LocalDate doneDateTime,

    @NotNull 
    TaskPriority priority
) {

}
