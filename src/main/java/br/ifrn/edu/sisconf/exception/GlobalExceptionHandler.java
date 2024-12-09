package br.ifrn.edu.sisconf.exception;

import br.ifrn.edu.sisconf.utils.keycloak.exceptions.AuthenticateClientFailedException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

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

    @ExceptionHandler(AuthenticateClientFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAuthenticateClientFailedException(
            AuthenticateClientFailedException exception,
            WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }
}
