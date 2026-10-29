package com.flee1son_dev.homework_spring;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Modifying 
    @Query("""
        update TaskEntity t
        set t.status = :status,
            t.doneDate = :doneDate
        where t.id = :id
        """)
    void setStatusToComplete(
        @Param("id") Long id,
        @Param("status") TaskStatus Status,
        @Param("doneDate") LocalDate doneDate
    );
}
