package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequestDTO {
    @NotNull(message = "Categoria não pode ser vazio")
    private CustomerCategory category;

    @NotNull(message = "Pessoa não pode ser vazio")
    @Valid
    private PersonUpdateRequestDTO person;
}
