package com.flee1son_dev.homework_spring.tasks.avaiability;

/**
 * CheckAvailabilityResponse
 */
public record CheckAvailabilityResponse(
    String message,
    AvailabilityStatus status
) {

}
