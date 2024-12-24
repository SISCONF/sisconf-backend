package br.ifrn.edu.sisconf.dto.keycloak;

public record UserRegistrationResponse(
        String keycloakId, String firstName, String lastName, String email) {
}
