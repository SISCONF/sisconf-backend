package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonCreateRequestDTO {
    @NotBlank(message = "firstName must not be blank")
    private String firstName;

    @NotBlank(message = "lastName must not be blank")
    private String lastName;

    @NotBlank(message = "CPF must not be blank")
    @NotNull
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must follow XXX.XXX.XXX-XX format")
    private String cpf;

    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
    private String cnpj;

    @NotBlank(message = "You must provide a password")
    @Size(min = 8, max = 16, message = "Password must be 8 to 16 characters")
    private String password;

    @NotBlank(message = "Confirm password must not be blank")
    @Size(min = 8, max = 16, message = "Confirm passsword must be 8 to 16 characters")
    private String password2;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "\\(\\d{2}\\) 9\\d{4}-\\d{4}", message = "Phone format must match (XX) XXXXX-XXXX")
    private String phone;

    @NotBlank(message = "Email cant be blank")
    @Email
    private String email;

    @NotNull
    private AddressCreateRequestDTO address;
}
