package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntrepreneurCreateRequestDTO {
    @NotBlank(message = "O Nome da Empresa não pode ser vazio")
    @Size(min = 1, max = 128, message = "O Nome da Empresa deve conter de 1 a 128 caracteres")
    private String businessName;

    @Valid
    private PersonCreateRequestDTO person;
}
