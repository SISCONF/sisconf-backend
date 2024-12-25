package br.ifrn.edu.sisconf.dto.keycloak;

public record UserRegistrationRecord(
        String firstName, String lastName, String password, String email) {
}
