package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityService cityService;

    public Person getByKeycloakId(String keycloakId) {
        return personRepository.findByKeycloakId(keycloakId).orElseThrow(() -> new ResourceNotFoundException(
            String.format("Usuário informado não existe")
        ));
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
        cityService.getById(personRequestDTO.getAddress().getCity());
    }
}
