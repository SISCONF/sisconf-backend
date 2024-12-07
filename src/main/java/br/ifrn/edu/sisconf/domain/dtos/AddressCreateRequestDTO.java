package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressCreateRequestDTO {
    @NotBlank(message = "Rua deve ser preenchida")
    @NotNull
    private String street;

    @NotBlank(message = "O CEP deve ser preenchido")
    @NotNull
    private Integer zip_code;

    @NotBlank(message = "Neighbourhood must not be blank")
    @NotNull
    private String neighbourhood;

    @NotBlank(message = "Number must not be blank")
    @NotNull
    private Integer number;

    @NotBlank(message = "City (id) must not be blank")
    @NotNull
    private Long city;
}
