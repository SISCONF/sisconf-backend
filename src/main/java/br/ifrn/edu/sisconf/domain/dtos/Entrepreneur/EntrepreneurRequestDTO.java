package br.ifrn.edu.sisconf.domain.dtos.Entrepreneur;

import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrepreneurRequestDTO {
    @NotBlank(message = "O Nome da Empresa não pode ser vazio")
    @Size(max = 128, message = "O Nome da Empresa deve conter de 1 a 128 caracteres")
    private String businessName;

    @NotNull(message = "Pessoa não pode ser vazia")
    @Valid
    private PersonRequestDTO person;
}
