package br.ifrn.edu.sisconf.domain.dtos;

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
public class CustomerCreateRequestDTO {
    @NotNull(message = "Category must not be null")
    private CustomerCategory category;

    @NotNull(message = "Person must not be null")
    @Valid
    private PersonCreateRequestDTO person;
}
