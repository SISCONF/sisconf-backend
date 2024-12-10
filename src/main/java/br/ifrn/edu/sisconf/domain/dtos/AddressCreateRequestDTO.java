package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateRequestDTO {
    @NotBlank(message = "Rua deve ser preenchida")
    private String street;

    @NotBlank(message = "O CEP deve ser preenchido")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must follow XXXXX-XXX format")
    private String zipCode;

    @NotBlank(message = "Neighbourhood must not be blank")
    private String neighbourhood;

    @NotBlank(message = "Number must not be blank")
    private Integer number;

    @NotBlank(message = "City (id) must not be blank")
    private Long city;
}
