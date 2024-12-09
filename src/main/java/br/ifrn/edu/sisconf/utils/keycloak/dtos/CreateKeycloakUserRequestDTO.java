package br.ifrn.edu.sisconf.utils.keycloak.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateKeycloakUserRequestDTO {
    private String username;
    private String email;
    private List<CreateKeycloakUserCredentials> credentials;
}
