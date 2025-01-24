package com.tutorial.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.tutorial.entity.TaskEntity;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, String> getFieldMappings(Class<?> clazz) {
        Map<String, String> mappings = new HashMap<>();
        objectMapper.getSerializationConfig()
                .introspect(objectMapper.constructType(clazz))
                .findProperties()
                .forEach(property -> {
                    AnnotatedField field = property.getField();
                    String jsonPropertyName = property.getName();
                    if (field != null) {
                        String javaFieldName = field.getName();
                        mappings.put(javaFieldName, jsonPropertyName);
                    }
                });
        return mappings;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.info("Exception occurred:" + ex.getMessage());
        Map<String, Object> errorDetails = new HashMap<>();
        // Extract JSON property names dynamically
        Map<String, String> fieldMappings = getFieldMappings(TaskEntity.class);

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = fieldMappings.getOrDefault(error.getField(), error.getField());
            errorDetails.put(fieldName, error.getDefaultMessage());
        }

        errorDetails.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        logger.info("Received Bad Request Exception:" + ex.toString());
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("message", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("message", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
