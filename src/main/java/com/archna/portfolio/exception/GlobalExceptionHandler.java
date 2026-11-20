package com.archna.portfolio.exception;

import com.archna.portfolio.contact.ContactResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ContactResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(". "));
        return ResponseEntity.badRequest().body(new ContactResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ContactResponse> handleUnreadableRequest() {
        return ResponseEntity.badRequest()
                .body(new ContactResponse("Request body is missing or invalid."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ContactResponse> handleUnexpectedError(Exception exception) {
        // Keep technical details in server logs; return a safe message to visitors.
        LOGGER.error("Unexpected error while processing a portfolio contact request", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ContactResponse("Something went wrong while receiving your message. Please try again later."));
    }
}
