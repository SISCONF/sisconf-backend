package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EntrepreneurService {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private EntrepreneurMapper entrepreneurMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private StockService stockService;

    private void validateEntrepreneurCreation(PersonRequestDTO personRequestDTO) {
        personService.validatePersonCreation(personRequestDTO);
        personService.throwIfCnpjIsNotUnique(personRequestDTO.getCnpj(), null);
    }

    private void validateEntrepreneurUpdate(PersonRequestDTO personRequestDTO, Long id) {
        personService.validatePersonUpdate(personRequestDTO, id);
        personService.throwIfCnpjIsNotUnique(personRequestDTO.getCnpj(), id);
    }

    public EntrepreneurResponseDTO getByKeycloakId(String keycloakId) {
        var entrepreneur = entrepreneurRepository.findByPersonKeycloakId(keycloakId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Empreendedor não encontrado"
            ));
        return entrepreneurMapper.toResponseDTO(entrepreneur);
    }

    public EntrepreneurResponseDTO save(EntrepreneurRequestDTO entrepreneurRequestDTO) {
        PersonRequestDTO personRequestDTO = entrepreneurRequestDTO.getPerson();
        this.validateEntrepreneurCreation(personRequestDTO);

        var userRegistrationRecord = new UserRegistrationRecord(
                personRequestDTO.getFirstName(),
                personRequestDTO.getLastName(),
                personRequestDTO.getPassword(),
                personRequestDTO.getEmail(),
                KeycloakConstants.ENTREPRENEUR_GROUP_NAME
        );
        UserRegistrationResponse userRegistrationResponse = keycloakUserService
            .create(userRegistrationRecord);

        Entrepreneur entrepreneur = entrepreneurMapper.toEntity(entrepreneurRequestDTO);
        entrepreneur.getPerson().setKeycloakId(userRegistrationResponse.keycloakId());
        try {
            var savedEntrepreneur = entrepreneurRepository.save(entrepreneur);
            stockService.save(entrepreneur);
            return entrepreneurMapper.toResponseDTO(savedEntrepreneur);
        } catch (Exception exception) {
            keycloakUserService.deleteById(userRegistrationResponse.keycloakId());
            throw exception;
        }
    }

    public EntrepreneurResponseDTO update(
        Long id, 
        EntrepreneurRequestDTO entrepreneurRequestDTO,
        String loggedEntrepreneurKeycloakId
    ) {

        PersonRequestDTO personRequestDTO = entrepreneurRequestDTO.getPerson();

        var entrepreneur = entrepreneurRepository
            .findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException(
                    String.format("Empreendedor com id %d não existe", id)
                )
            );

        personService.throwIfLoggedPersonIsDifferentFromPersonResource(
            loggedEntrepreneurKeycloakId, 
            entrepreneur.getPerson()
        );
        this.validateEntrepreneurUpdate(personRequestDTO, entrepreneur.getPerson().getId());

        var userUpdateRecord = new UserUpdateRecord(
                entrepreneur.getPerson().getKeycloakId(),
                personRequestDTO.getFirstName(),
                personRequestDTO.getLastName()
        );
        keycloakUserService.update(userUpdateRecord);

        try {
            entrepreneurMapper.updateEntityFromDTO(entrepreneurRequestDTO, entrepreneur);
            var updatedEntrepreneur = entrepreneurRepository.save(entrepreneur);
            return entrepreneurMapper.toResponseDTO(updatedEntrepreneur);
        } catch (Exception exception) {
            // Must revert the changes made by the keycloakUserService.update method from above
            var oldUserRecord = new UserUpdateRecord(
                    entrepreneur.getPerson().getKeycloakId(),
                    entrepreneur.getPerson().getFirstName(),
                    entrepreneur.getPerson().getLastName()
            );
            keycloakUserService.update(oldUserRecord);
            throw exception;
        }
    }

    public void deleteById(Long id, String loggedEntrepreneurKeycloakId) {
        var entrepreneur = entrepreneurRepository
            .findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException(
                    String.format("Empreendedor com id %d não existe", id)
                )
            );
        personService.throwIfLoggedPersonIsDifferentFromPersonResource(
            loggedEntrepreneurKeycloakId, 
            entrepreneur.getPerson()
        );
        keycloakUserService.deleteById(entrepreneur.getPerson().getKeycloakId());
        entrepreneurRepository.deleteById(id);
    }
}
