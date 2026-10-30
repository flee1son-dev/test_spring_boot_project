package com.flee1son_dev.homework_spring.tasks.avaiability;

/**
 * CheckAvailabilityRequest
 */
public record CheckAvailabilityRequest(
    Long id,
    Long assignedUserId
) {
}
