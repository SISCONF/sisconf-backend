package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.configs.KeycloakProperties;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.Person.LoginRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.LoginResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.exception.keycloak.InvalidCredentialsException;
import br.ifrn.edu.sisconf.exception.keycloak.KeycloakUnavailableException;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.PersonRepository;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.ProcessingException;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private KeycloakProperties keycloakProperties;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            Keycloak keycloakUserClient = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .username(loginRequestDTO.getEmail())
                .password(loginRequestDTO.getPassword())
                .build();
            AccessTokenResponse tokenResponse = keycloakUserClient.tokenManager().getAccessToken();
            return new LoginResponseDTO(
                tokenResponse.getToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getExpiresIn(),
                tokenResponse.getRefreshExpiresIn(),
                tokenResponse.getTokenType(),
                tokenResponse.getNotBeforePolicy(),
                tokenResponse.getSessionState(),
                tokenResponse.getScope()
            );
        } catch (NotAuthorizedException exception) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        } catch (ProcessingException exception) {
            throw new KeycloakUnavailableException("Serviço de usuários indisponível");
        }
    }

    public void throwIfLoggedPersonIsDifferentFromPersonResource(
        String loggedPersonKeycloakId,
        Person person
    ) {
        if (!person.getKeycloakId().equals(loggedPersonKeycloakId)) {
            throw new ResourceNotFoundException(
                "Usuário não encontrado"
            );
        }
    }

    public void throwIfCpfIsNotUnique(String cpf, Long id) {
        var errorMsg = String.format("Usuário com CPF %s já existe", cpf);
        if (id != null) {
            if (personRepository.existsByCpfAndIdNot(cpf, id)) {
                throw new BusinessException(errorMsg);
            }
        } else {
            if (personRepository.existsByCpf(cpf)) {
                throw new BusinessException(errorMsg);
            }
        }
    }

    public void throwIfPasswordsDontMatch(
        String password,
        String password2
    ) {
        if (!password.equals(password2)) {
            throw new BusinessException("As senhas precisam ser iguais");
        }
    }

    public void throwIfEmailIsNotUnique(String email) {
        if (personRepository.existsByEmail(email)) {
            var errorMsg = String.format("Usuário com e-mail %s já existe", email);
            throw new BusinessException(errorMsg);
        }
    }

    public void throwIfCnpjIsNotUnique(String cnpj, Long id) {
        var errorMsg = String.format("Empresa com CNPJ %s já existe", cnpj);
        if (id != null) {
            if (personRepository.existsByCnpjAndIdNot(cnpj, id)) {
                throw new BusinessException(errorMsg);
            }
        } else {
            if (personRepository.existsByCnpj(cnpj)) {
                throw new BusinessException(errorMsg);
            }
        }
    }

    public void throwIfPhoneIsNotUnique(String phone, Long id) {
        var errorMsg = String.format("Usuário com telefone %s já existe", phone);
        if (id != null) {
            if (personRepository.existsByPhoneAndIdNot(phone, id)) {
                throw new BusinessException(errorMsg);
            }
        } else {
            if (personRepository.existsByPhone(phone)) {
                throw new BusinessException(errorMsg);
            }
        }
    }

    public void validatePersonCreation(PersonRequestDTO personRequestDTO) {
        this.throwIfCpfIsNotUnique(personRequestDTO.getCpf(), null);
        this.throwIfEmailIsNotUnique(personRequestDTO.getEmail());
        this.throwIfPasswordsDontMatch(
            personRequestDTO.getPassword(),
            personRequestDTO.getPassword2()
        );
        this.throwIfPhoneIsNotUnique(personRequestDTO.getPhone(), null);
        cityService.getById(personRequestDTO.getAddress().getCity());
    }

    public void validatePersonUpdate(PersonRequestDTO personRequestDTO, Long id) {
        this.throwIfCpfIsNotUnique(personRequestDTO.getCpf(), id);
        this.throwIfPhoneIsNotUnique(personRequestDTO.getPhone(), id);
        if (personRequestDTO.getEmail() != null) {
            if (!personRequestDTO.getEmail().equals("")) {
                throw new BusinessException("Email não pode ser atualizado");
            }
        }
        cityService.getById(personRequestDTO.getAddress().getCity());
    }

    public Person savePersonCity(Person person, Long cityId) {
        var city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException(
            String.format("Cidade com id %d não encontrada", cityId)
        ));
        person.getAddress().setCity(city);
        return personRepository.save(person);
    }
}
