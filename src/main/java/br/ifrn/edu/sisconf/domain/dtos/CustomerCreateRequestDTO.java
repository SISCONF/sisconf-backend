package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerCreateRequestDTO {
    @NotNull(message = "Category must not be null")
    private CustomerCategory category;

    @NotNull
    @NotBlank(message = "Person must not be blank")
    private PersonCreateRequestDTO person;
}
