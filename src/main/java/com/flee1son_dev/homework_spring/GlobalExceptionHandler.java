package com.flee1son_dev.homework_spring;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice 
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
        Exception e
    ) {
        log.error("Handle exception");
        var errorEntity = new ErrorResponseDto(
            "Internal Server Error", 
            e.getMessage(), 
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorEntity);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerEntityNotFoundException(
        EntityNotFoundException e
    ) {
        log.error("Handle entityNotFoundException");
        var errorEntity = new ErrorResponseDto(
            "Entity Not Found", 
            e.getMessage(), 
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorEntity);
    }


    @ExceptionHandler(exception = {
        IllegalArgumentException.class,
        IllegalStateException.class,
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(
        Exception e
    ) {
        log.error("Handle badRequestException");
        var errorEntity = new ErrorResponseDto(
            "Bad Request", 
            e.getMessage(), 
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorEntity);
    }
}
