package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonUpdateRequestDTO {
    @NotBlank(message = "firstName não pode ser vazio")
    private String firstName;

    @NotBlank(message = "lastName não pode ser vazio")
    private String lastName;

    @NotBlank(message = "CPF não pode ser vazio")
    @NotNull
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve seguir o formato XXX.XXX.XXX-XX")
    private String cpf;

    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
    private String cnpj;

    @NotBlank(message = "Phone não pode ser vazio")
    @Pattern(regexp = "\\(\\d{2}\\) 9\\d{4}-\\d{4}", message = "Phone deve seguir formato (XX) XXXXX-XXXX")
    private String phone;

    @Valid
    private AddressRequestDTO address;
}
