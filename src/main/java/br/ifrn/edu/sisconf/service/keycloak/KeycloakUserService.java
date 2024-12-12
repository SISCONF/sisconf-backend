package br.ifrn.edu.sisconf.service.keycloak;

import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakUserService {
    UserRegistrationResponse createUser(UserRegistrationRecord userRegistrationRecord);
    UserRegistrationResponse updateUser(UserUpdateRecord userUpdateRecord);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
}
