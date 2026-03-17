package br.ifrn.edu.sisconf.domain.dtos.Customer;

import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {
    @NotNull(message = "Categoria não pode ser vazio")
    private CustomerCategory category;

    @NotNull(message = "Pessoa não pode ser vazio")
    @Valid
    private PersonRequestDTO person;
}
