package com.flee1son_dev.homework_spring.tasks.avaiability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/tasks/availability")
public class TaskAvailabilityController {
    private static final Logger log = LoggerFactory.getLogger(TaskAvailabilityService.class);

    private final TaskAvailabilityService availabilityService;

    public TaskAvailabilityController(TaskAvailabilityService availabilityService){
        this.availabilityService = availabilityService;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(
        @Valid  CheckAvailabilityRequest request
    ) {
        log.info("Called method checkAvailability");
        boolean isAvailable = availabilityService.isTaskAvailable(request.id(), request.assignedUserId());
        var message = isAvailable ? "Task available to start with assigned user"
                                    : "Task not available to start with assigned user";
        var status = isAvailable ? AvailabilityStatus.AVAILABLE
                                    : AvailabilityStatus.RESERVED;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CheckAvailabilityResponse(message, status));
    }
    
}
