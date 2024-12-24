package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrepreneurUpdateRequestDTO {
    @NotBlank(message = "O Nome da Empresa não pode ser vazio")
    @Size(min = 1, max = 128, message = "O Nome da Empresa deve conter de 1 a 128 caracteres")
    private String businessName;

    @Valid
    private PersonUpdateRequestDTO person;
}
