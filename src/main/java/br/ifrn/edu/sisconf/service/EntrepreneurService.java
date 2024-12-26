package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.*;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private CityService cityService;

    private void throwIfCnpjInvalid(PersonUpdateRequestDTO personUpdateRequestDTO) {
        String cnpj = personUpdateRequestDTO.getCnpj();
        String cnpjErrorMsg = "CNPJ não pode ser vazio";

        // Two separate 'ifs' are needed because calling isEmpty implies cnpj is not null
        if (cnpj == null) {
            throw new BusinessException(cnpjErrorMsg);
        }

        if (cnpj.isEmpty()) {
            throw new BusinessException(cnpjErrorMsg);
        }
        personService.throwIfCnpjIsNotUnique(personUpdateRequestDTO);
    }

    private void validateEntrepreneurCreation(PersonCreateRequestDTO personCreateRequestDTO) {
        personService.validatePersonCreation(personCreateRequestDTO);
        this.throwIfCnpjInvalid(personCreateRequestDTO);
    }

    private void validateEntrepreneurUpdate(PersonUpdateRequestDTO personUpdateRequestDTO) {
        personService.validatePersonUpdate(personUpdateRequestDTO);
        this.throwIfCnpjInvalid(personUpdateRequestDTO);
    }

    public EntrepreneurResponseDTO getById(Long id) {
        Entrepreneur entrepreneur = entrepreneurRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Empreendedor com id %d não existe", id)
                ));
        return entrepreneurMapper.toResponseDTO(entrepreneur);
    }

    public List<EntrepreneurResponseDTO> getAll() {
        return entrepreneurMapper.toDTOList(entrepreneurRepository.findAll());
    }

    public EntrepreneurResponseDTO save(EntrepreneurCreateRequestDTO entrepreneurCreateRequestDTO) {
        PersonCreateRequestDTO personCreateRequestDTO = entrepreneurCreateRequestDTO.getPerson();
        this.validateEntrepreneurCreation(personCreateRequestDTO);

        var userRegistrationRecord = new UserRegistrationRecord(
                personCreateRequestDTO.getFirstName(),
                personCreateRequestDTO.getLastName(),
                personCreateRequestDTO.getPassword(),
                personCreateRequestDTO.getEmail(),
                KeycloakConstants.ENTREPRENEUR_GROUP_NAME
        );
        UserRegistrationResponse userRegistrationResponse = keycloakUserService.create(userRegistrationRecord);

        Entrepreneur entrepreneur = entrepreneurMapper.toEntity(entrepreneurCreateRequestDTO);
        entrepreneur.getPerson().setKeycloakId(userRegistrationResponse.keycloakId());
        try {
            var savedEntrepreneur = entrepreneurRepository.save(entrepreneur);
            return entrepreneurMapper.toResponseDTO(savedEntrepreneur);
        } catch (Exception exception) {
            keycloakUserService.deleteById(userRegistrationResponse.keycloakId());
            throw exception;
        }
    }

    public EntrepreneurResponseDTO update(Long id, EntrepreneurUpdateRequestDTO entrepreneurUpdateRequestDTO) {
        PersonUpdateRequestDTO personUpdateRequestDTO = entrepreneurUpdateRequestDTO.getPerson();
        this.validateEntrepreneurUpdate(personUpdateRequestDTO);

        var entrepreneur = entrepreneurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Empreendedor com id %d não existe", id)
        ));

        var userUpdateRecord = new UserUpdateRecord(
                entrepreneur.getPerson().getKeycloakId(),
                personUpdateRequestDTO.getFirstName(),
                personUpdateRequestDTO.getLastName()
        );
        keycloakUserService.update(userUpdateRecord);

        try {
            entrepreneurMapper.updateEntityFromDTO(entrepreneurUpdateRequestDTO, entrepreneur);
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

    public void deletebyId(Long id) {
        var entrepreneur = entrepreneurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Empreendedor com id %d não existe", id)
        ));
        entrepreneurRepository.deleteById(id);
        keycloakUserService.deleteById(entrepreneur.getPerson().getKeycloakId());
    }
}
