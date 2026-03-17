package br.ifrn.edu.sisconf.service.keycloak;

import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakUserService {
    UserRegistrationResponse create(UserRegistrationRecord userRegistrationRecord);
    UserRegistrationResponse update(UserUpdateRecord userUpdateRecord);
    UserRepresentation getById(String userId);
    void deleteById(String userId);
}
