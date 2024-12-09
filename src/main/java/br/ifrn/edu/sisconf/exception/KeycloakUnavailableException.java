package br.ifrn.edu.sisconf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class KeycloakUnavailableException extends RuntimeException {
    public KeycloakUnavailableException(String message) {
        super(message);
    }
}
