package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.PersonResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;

public class PersonTestUtil {
    public static PersonRequestDTO createValidPersonCreateRequestDTO(String cnpj) {
        return new PersonRequestDTO(
            "Teste",
            "Teste 2",
            "123.456.789-10",
            cnpj,
            "(11) 91111-1111",
            "abcd1234",
            "abcd1234",
            "teste@gmail.com",
            AddressTestUtil.createValidAddressRequestDTO()
        );

    }

    public static UserRegistrationRecord createUserRegistrationRecord(
        PersonRequestDTO personRequestDTO, 
        String group
    ) {
        return new UserRegistrationRecord(
            personRequestDTO.getFirstName(),
            personRequestDTO.getLastName(),
            personRequestDTO.getPassword(),
            personRequestDTO.getEmail(),
            group
        );
    }

    public static PersonResponseDTO toResponseDTO(Person person) {
        return new PersonResponseDTO(
            person.getId(),
            person.getKeycloakId(),
            person.getFirstName(),
            person.getLastName(),
            person.getEmail(),
            person.getCpf(),
            person.getCnpj(),
            person.getPhone(),
            AddressTestUtil.toResponseDTO(person.getAddress())
        );
    }
}
