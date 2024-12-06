package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonCreateRequestDTO {
    @NotBlank(message = "Nome não pode ser vazio")
    @NotNull
    private String firstName;

    @NotBlank(message = "Sobrenome não pode ser vazio")
    @NotNull
    private String lastName;

    @Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos")
    @NotNull(message = "CPF must no be null")
    private Integer cpf;

    @Size(min = 14, max = 14, message = "O CNPJ deve conter 14 dígitos")
    @NotNull(message = "CNPJ must not be null")
    private Integer cnpj;

    @NotBlank(message = "Senha não pode ser vazia")
    @NotNull
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank(message = "Confirmar senha não pode ser vazio")
    @NotNull
    @Size(min = 8, max = 16)
    private String password2;

    @NotBlank(message = "Telefone não pode ser vazio")
    @NotNull
    @Pattern(regexp = "\\(\\d{2}\\) 9\\d{4}-\\d{4}", message = "O telefone deve seguir o formato (XX) XXXXX-XXXX")
    private String phone;

    @NotBlank(message = "Email não pode ser vazio")
    @NotNull
    @Email
    private String email;

    @NotNull
    private AddressCreateRequestDTO address;
}
