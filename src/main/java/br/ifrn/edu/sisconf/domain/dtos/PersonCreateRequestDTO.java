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
    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 16, message = "Senha deve ter de 8 a 16 caracteres")
    private String password;

    @NotBlank(message = "Confirmar senha não pode ser vazia")
    @Size(min = 8, max = 16, message = "Confirmar senha deve ter de 8 a 16 caracteres")
    private String password2;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email deve seguir o formato de emails")
    private String email;
}
