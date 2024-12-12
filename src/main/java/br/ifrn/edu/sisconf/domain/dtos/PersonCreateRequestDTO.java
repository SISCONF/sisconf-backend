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
public class PersonCreateRequestDTO extends PersonUpdateRequestDTO {
    @NotBlank(message = "You must provide a password")
    @Size(min = 8, max = 16, message = "Password must be 8 to 16 characters")
    private String password;

    @NotBlank(message = "Confirm password must not be blank")
    @Size(min = 8, max = 16, message = "Confirm passsword must be 8 to 16 characters")
    private String password2;

    @NotBlank(message = "Email cant be blank")
    @Email
    private String email;
}
