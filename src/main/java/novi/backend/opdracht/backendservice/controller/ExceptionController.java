package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        logger.error("BadRequestException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Bad request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProductNameTooLongException.class)
    public ResponseEntity<ErrorResponse> handleProductNameTooLongException(ProductNameTooLongException ex) {
        logger.error("ProductNameTooLongException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Product name too long"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("AccessDeniedException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Access denied"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Resource not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UsernameExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameExistsException(UsernameExistsException ex) {
        logger.error("UsernameExistsException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Username already exists"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        logger.error("AuthenticationException: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Authentication failed"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation exception: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("General exception: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
