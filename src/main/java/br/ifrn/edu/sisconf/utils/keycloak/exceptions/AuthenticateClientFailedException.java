package br.ifrn.edu.sisconf.utils.keycloak.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AuthenticateClientFailedException extends RuntimeException {
    public AuthenticateClientFailedException(String message) {
        super(message);
    }
}
