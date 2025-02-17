package ru.t1.java.demo.controllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.t1.java.demo.dto.EntityNotFoundDto;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<EntityNotFoundDto> handleEntityNotFoundException(NoSuchElementException exception) {
        log.error("No such element exception: " + exception);
        EntityNotFoundDto errorDto = EntityNotFoundDto.builder()
                .exceptionName(exception.getClass().getCanonicalName())
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.toString())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

}
