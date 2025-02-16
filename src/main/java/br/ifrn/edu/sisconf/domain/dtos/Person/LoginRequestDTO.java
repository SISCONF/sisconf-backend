package br.ifrn.edu.sisconf.domain.dtos.Person;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email não pode ser vazio")
    private String email;
    @NotBlank(message = "Senha não pode ser vazio")
    private String password;
}
