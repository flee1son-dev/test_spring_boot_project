package com.flee1son_dev.homework_spring.tasks;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT t FROM TaskEntity t
        WHERE (:creatorId IS NULL OR t.creatorId = :creatorId)
        AND (:assignedUserId IS NULL OR t.assignedUserId = :assignedUserId)
        """)
    List<TaskEntity> searchAllByFilter(
        @Param("creatorId") Long CreatorId, 
        @Param("assignedUserId") Long assignedUserId,
        Pageable pageable
    );

    @Query("""
        SELECT t.id FROM TaskEntity t
        WHERE t.assignedUserId = :assignedId
        and t.status = :status
        """)
    List<Long> findLimitedAssignedUserIds(
        @Param("creatorId") Long creatorId,
        @Param("assignedId") Long assignedUserId,
        @Param("status") TaskStatus status
    );
}
