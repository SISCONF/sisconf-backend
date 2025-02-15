package br.ifrn.edu.sisconf.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private Integer status;
        private String message;
        private String path;

        // Construtor adicional para mensagem simples
        public ErrorResponse(String message) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
        }

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException exception,
        WebRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .message(exception.getMessage())
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .build();
    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(UnrecognizedPropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUnrecognizedPropertyException(
            UnrecognizedPropertyException exception,
            WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException exception,
        WebRequest request
    ) {
        List<String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> String.format("%s (%s)", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
        
        String errorMessage = "Erro de validação nos seguintes campos: " + String.join(", ", errorMessages);
    
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .message(errorMessage)
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}
}
