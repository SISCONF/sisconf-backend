package br.ifrn.edu.sisconf.dto.keycloak;

public record UserUpdateRecord(
        String keycloakId, String firstName, String lastName
) {
}
