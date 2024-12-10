package br.ifrn.edu.sisconf.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDTO {
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String cpf;
    private String cnpj;
    private AddressResponseDTO address;
}
