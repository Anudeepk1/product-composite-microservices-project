package com.library.common_service.utils.exceptions;

import com.library.common_service.utils.core.http.HttpErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public Mono<ResponseEntity<HttpErrorInfo>> handleInvalidInputException(ServerWebExchange exchange, InvalidInputException ex) {
        return Mono.just(ResponseEntity
                .unprocessableEntity()
                .body(createHttpErrorInfo(UNPROCESSABLE_ENTITY, exchange, ex)));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<HttpErrorInfo>> handleEntityNotFoundException(ServerWebExchange exchange, EntityNotFoundException ex) {
        return Mono.just(ResponseEntity
                .status(NOT_FOUND)
                .body(createHttpErrorInfo(NOT_FOUND, exchange, ex)));
    }

    @ExceptionHandler(EventProcessingException.class)
    public Mono<ResponseEntity<HttpErrorInfo>> handleEventProcessingException(ServerWebExchange exchange, EntityNotFoundException ex) {
        return Mono.just(ResponseEntity
                .status(NOT_FOUND)
                .body(createHttpErrorInfo(NOT_FOUND, exchange, ex)));
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerWebExchange exchange, Exception ex) {
        return new HttpErrorInfo(
                ZonedDateTime.now(),
                exchange.getRequest().getPath().value(),
                httpStatus,
                ex.getMessage()
        );
    }
}
