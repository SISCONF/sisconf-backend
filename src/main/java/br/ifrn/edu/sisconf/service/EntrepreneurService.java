package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
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

    private void validateEntrepreneurCreation(PersonCreateRequestDTO personCreateRequestDTO) {
        personService.throwIfCpfIsNotUnique(personCreateRequestDTO);
        personService.throwIfEmailIsNotUnique(personCreateRequestDTO);
        personService.throwIfPasswordsDontMatch(personCreateRequestDTO);
        personService.throwIfPhoneIsNotUnique(personCreateRequestDTO);
        String cnpjErrorMsg = "CNPJ não pode ser vazio";
        if (personCreateRequestDTO.getCnpj() == null) {
            throw new BusinessException(cnpjErrorMsg);
        }
        if (personCreateRequestDTO.getCnpj().isEmpty()) {
            throw new BusinessException(cnpjErrorMsg);
        }
        personService.throwIfCnpjIsNotUnique(personCreateRequestDTO);
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
}
