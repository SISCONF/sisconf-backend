package br.ifrn.edu.sisconf.utils.keycloak.dtos;

import lombok.Data;

@Data
public class CreateKeycloakUserCredentials {
    private String type;
    private String value;
    private boolean temporary;
}
