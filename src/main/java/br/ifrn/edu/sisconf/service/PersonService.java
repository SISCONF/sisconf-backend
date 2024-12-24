package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Person;
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

    public void throwIfCpfIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO) {
        String cpf =personCreateRequestDTO.getCpf();
        if (personRepository.existsByCpf(cpf)) {
            var errorMsg = String.format("Usuário com CPF %s já existe", cpf);
            throw new BusinessException(errorMsg);
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

    public void throwIfCnpjIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO) {
        String cnpj = personCreateRequestDTO.getCnpj();
        if (personRepository.existsByCnpj(cnpj)) {
            var errorMsg = String.format("Empresa com CNPJ %s já existe", cnpj);
            throw new BusinessException(errorMsg);
        }
    }

    public void throwIfPhoneIsNotUnique(PersonUpdateRequestDTO personCreateRequestDTO) {
        String phone = personCreateRequestDTO.getPhone();
        if (personRepository.existsByPhone(phone)) {
            var errorMsg = String.format("Usuário com telefone %s já existe", phone);
            throw new BusinessException(errorMsg);
        }
    }
}
