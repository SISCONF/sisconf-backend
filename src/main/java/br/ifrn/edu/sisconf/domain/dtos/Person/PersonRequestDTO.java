package br.ifrn.edu.sisconf.domain.dtos.Person;

import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.CreateEntrepreneurGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequestDTO {
    @NotBlank(
        message = "Nome não pode ser vazio",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    @Size(
        max = 255, 
        message = "Nome deve ter até 255 caracteres",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    private String firstName;

    @NotBlank(
        message = "Sobrenome não pode ser vazio",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    @Size(
        max = 255,
        message = "Sobrenome deve ter até 255 caracteres",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    private String lastName;

    @NotBlank(
        message = "CPF não pode ser vazio",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    @Pattern(
        regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", 
        message = "CPF deve seguir o formato XXX.XXX.XXX-XX",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    private String cpf;

    @Pattern(
        regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", 
        message = "CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    @NotBlank(
        message = "CNPJ não pode ser vazio",
        groups = {CreateEntrepreneurGroup.class}
    )
    private String cnpj;

    @NotBlank(
        message = "Phone não pode ser vazio",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    @Pattern(
        regexp = "\\(\\d{2}\\) 9\\d{4}-\\d{4}", 
        message = "Phone deve seguir formato (XX) XXXXX-XXXX",
        groups = {CreatePersonGroup.class, UpdatePersonGroup.class}
    )
    private String phone;

    @NotBlank(
        message = "Senha não pode ser vazia",
        groups = CreatePersonGroup.class
    )
    @Size(
        min = 8, 
        max = 16, 
        message = "Senha deve ter de 8 a 16 caracteres",
        groups = CreatePersonGroup.class
    )
    private String password;

    @NotBlank(
        message = "Confirmar senha não pode ser vazia",
        groups = CreatePersonGroup.class
    )
    @Size(
        min = 8, 
        max = 16, 
        message = "Confirmar senha deve ter de 8 a 16 caracteres",
        groups = CreatePersonGroup.class
    )
    private String password2;

    @NotBlank(
        message = "Email não pode ser vazio",
        groups = CreatePersonGroup.class
    )
    @Email(
        message = "Email deve seguir o formato de emails",
        groups = CreatePersonGroup.class
    )
    private String email;

    @Valid
    private AddressRequestDTO address;
}
