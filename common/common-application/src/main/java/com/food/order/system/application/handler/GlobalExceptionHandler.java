package com.food.order.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ErrorDTO handleOrderDomainException(Exception e) {
        e.printStackTrace();
        log.error("Error occurred: {}", e.getMessage());
        return ErrorDTO.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unknown error occurred").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseBody
    public ErrorDTO handleOrderDomainException(ValidationException e) {
        e.printStackTrace();
        ErrorDTO errorDTO;
        if (e instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) e);
            log.error("Error occurred: {}", violations);
            errorDTO = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations).build();
        } else {
            log.error("Error occurred: {}", e.getMessage());
            errorDTO = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(e.getMessage()).build();
        }
        return errorDTO;
    }

    private String extractViolationsFromException(ConstraintViolationException e) {
        e.printStackTrace();
        return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("->"));
    }
}
