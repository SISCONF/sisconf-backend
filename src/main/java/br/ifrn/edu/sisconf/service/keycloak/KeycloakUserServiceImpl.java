package br.ifrn.edu.sisconf.service.keycloak;

import br.ifrn.edu.sisconf.configs.KeycloakProperties;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.keycloak.KeycloakUnavailableException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public UserRegistrationResponse create(UserRegistrationRecord userRegistrationRecord) {
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
    public UserRepresentation getById(String userId) {
        return getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteById(String userId) {
        Response response = getUsersResource().delete(userId);
        Response.Status.Family statusFamily = Response.Status.Family.familyOf(response.getStatus());

        if (Objects.requireNonNull(statusFamily) != Response.Status.Family.SUCCESSFUL) {
            if (Objects.requireNonNull(statusFamily) == Response.Status.Family.CLIENT_ERROR) {
                throw new BusinessException(response.readEntity(String.class));
            }
            if (Objects.requireNonNull(statusFamily) == Response.Status.Family.SERVER_ERROR) {
                throw new KeycloakUnavailableException("falied contacting keycloak");
            }
        }

    }

    @Override
    public UserRegistrationResponse update(UserUpdateRecord userUpdateRecord) {
        try {
            UserResource userResource = getUsersResource().get(userUpdateRecord.keycloakId());
            UserRepresentation userRepresentation = userResource.toRepresentation();

            userRepresentation.setFirstName(userUpdateRecord.firstName());
            userRepresentation.setLastName(userUpdateRecord.lastName());
            userResource.update(userRepresentation);

            return new UserRegistrationResponse(
                    userRepresentation.getId(),
                    userRepresentation.getFirstName(),
                    userRepresentation.getLastName(),
                    userRepresentation.getEmail()
            );
        } catch (BadRequestException | InternalServerErrorException exception) {
            throw new BusinessException(exception.getMessage());
        }
    }
}
