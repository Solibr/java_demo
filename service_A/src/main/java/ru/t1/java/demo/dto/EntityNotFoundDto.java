package ru.t1.java.demo.dto;

import lombok.Builder;

@Builder
public record EntityNotFoundDto (
        String exceptionName,
        String message,
        String status
) {}