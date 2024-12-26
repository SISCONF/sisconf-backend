package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonUpdateRequestDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityService cityService;

    public void throwIfCpfIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO, Long id) {
        String cpf = personCreateRequestDTO.getCpf();
        var errorMsg = String.format("Usuário com CPF %s já existe", cpf);
        if (id != null) {
            if (personRepository.existsByCpfAndIdNot(cpf, id)) {
                System.out.println("EITA SOU TRUE");
                throw new BusinessException(errorMsg);
            }
        } else {
            if (personRepository.existsByCpf(cpf)) {
                throw new BusinessException(errorMsg);
            }
        }
    }

    public void throwIfPasswordsDontMatch(PersonCreateRequestDTO personCreateRequestDTO) {
        if (!personCreateRequestDTO.getPassword().equals(personCreateRequestDTO.getPassword2())) {
            throw new BusinessException("As senhas precisam ser iguais");
        }
    }

    public void throwIfEmailIsNotUnique(PersonCreateRequestDTO personCreateRequestDTO) {
        String email = personCreateRequestDTO.getEmail();
        if (personRepository.existsByEmail(email)) {
            var errorMsg = String.format("Usuário com e-mail %s já existe", email);
            throw new BusinessException(errorMsg);
        }
    }

    public void throwIfCnpjIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO, Long id) {
        String cnpj = personCreateRequestDTO.getCnpj();
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

    public void throwIfPhoneIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO, Long id) {
        String phone = personCreateRequestDTO.getPhone();
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

    public void validatePersonCreation(PersonCreateRequestDTO personCreateRequestDTO) {
        this.throwIfCpfIsNotUnique(personCreateRequestDTO, null);
        this.throwIfEmailIsNotUnique(personCreateRequestDTO);
        this.throwIfPasswordsDontMatch(personCreateRequestDTO);
        this.throwIfPhoneIsNotUnique(personCreateRequestDTO, null);
        cityService.getById(personCreateRequestDTO.getAddress().getCity());
    }

    public void validatePersonUpdate(PersonUpdateRequestDTO personUpdateRequestDTO, Long id) {
        this.throwIfCpfIsNotUnique(personUpdateRequestDTO, id);
        this.throwIfPhoneIsNotUnique(personUpdateRequestDTO, id);
        this.throwIfCnpjIsNotUnique(personUpdateRequestDTO, id);
        cityService.getById(personUpdateRequestDTO.getAddress().getCity());
    }
}
