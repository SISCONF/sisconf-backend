package br.ifrn.edu.sisconf.util;

import java.util.UUID;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.PersonResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;

public class PersonTestUtil {
    public static Person createValidPerson(String cnpj) {
        return new Person(
            UUID.randomUUID().toString(), 
            "Teste", 
            "Teste 2", 
            "teste@gmail.com", 
            "123.456.789-10", 
            cnpj, 
            "(11) 91111-1111", AddressTestUtil.createValidAddress()
        );
    }

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

    public static UserUpdateRecord createUserUpdateRecord(
        String keycloakId,
        PersonRequestDTO personRequestDTO
    ) {
        return new UserUpdateRecord(
            keycloakId,
            personRequestDTO.getFirstName(),
            personRequestDTO.getLastName()
        );
    }

    public static PersonRequestDTO toValidRequestDTO(Person person) {
        return new PersonRequestDTO(
            person.getFirstName(),
            person.getLastName(),
            person.getCpf(),
            person.getCnpj(),
            person.getPhone(),
            "abcd1234",
            "abcd1234",
            person.getEmail(),
            AddressTestUtil.toValidRequestDTO(person.getAddress())
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
