package br.ifrn.edu.sisconf.service.keycloak;

import br.ifrn.edu.sisconf.configs.KeycloakProperties;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.keycloak.KeycloakUnavailableException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {
    @Autowired
    private KeycloakProperties keycloakProperties;

    private Keycloak keycloak;

    @Autowired
    public KeycloakUserServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UsersResource getUsersResource() {
        RealmResource realm = keycloak.realm(keycloakProperties.getRealm());
        return realm.users();
    }

    @Override
    public UserRegistrationResponse createUser(UserRegistrationRecord userRegistrationRecord) {
        System.out.println(userRegistrationRecord.email());
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.email());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);

        user.setCredentials(list);

        UsersResource usersResource = getUsersResource();

        Response response = usersResource.create(user);
        Response.Status.Family statusFamily = Response.Status.Family.familyOf(response.getStatus());

        return switch (statusFamily) {
            case Response.Status.Family.SUCCESSFUL -> {
                String locationHeader = response.getHeaderString("Location");
                String userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                yield new UserRegistrationResponse(
                    userId, userRegistrationRecord.firstName(), userRegistrationRecord.lastName(), userRegistrationRecord.email()
                );
            }
            case Response.Status.Family.CLIENT_ERROR -> throw new BusinessException(response.readEntity(String.class));
            default -> throw new KeycloakUnavailableException("falied contacting keycloak");
        };
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId) {
        getUsersResource().delete(userId);
    }
}
