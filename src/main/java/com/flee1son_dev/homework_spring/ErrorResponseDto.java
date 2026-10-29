package com.flee1son_dev.homework_spring;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime errorTime
) {

}
