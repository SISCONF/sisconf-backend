package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message = "Rua deve ser preenchida")
    private String street;

    @NotBlank(message = "O CEP deve ser preenchido")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP precisa estar no formato XXXXX-XXX")
    private String zipCode;

    @NotBlank(message = "Bairro não pode ser vazio")
    private String neighbourhood;

    @NotBlank(message = "Número não pode ser vazio")
    private Integer number;

    @NotBlank(message = "Cidade não pode ser vazio")
    private Long city;
}
