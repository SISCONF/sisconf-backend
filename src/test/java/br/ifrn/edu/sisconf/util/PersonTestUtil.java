package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;

public class PersonTestUtil {
    public static PersonCreateRequestDTO createValidPersonCreateRequestDTO(String cnpj) {
        var dto = new PersonCreateRequestDTO(
            "abcd1234",
            "abcd1234",
            "teste@gmail.com"
        );
        dto.setFirstName("Teste");
        dto.setLastName("Teste 2");
        dto.setCnpj(cnpj);
        dto.setCpf("123.456.789-10");
        dto.setPhone("(11) 91111-1111");
        dto.setAddress(AddressTestUtil.createValidAddressRequestDTO());
        return dto;
    }

    public static UserRegistrationRecord createUserRegistrationRecord(PersonCreateRequestDTO personCreateRequestDTO, String group) {
        return new UserRegistrationRecord(
            personCreateRequestDTO.getFirstName(),
            personCreateRequestDTO.getLastName(),
            personCreateRequestDTO.getPassword(),
            personCreateRequestDTO.getEmail(),
            group
        );
    }
}
