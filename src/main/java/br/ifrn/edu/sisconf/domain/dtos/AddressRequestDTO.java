package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 128, message = "Rua deve ter no máximo 128 caracteres")
    private String street;

    @NotBlank(message = "O CEP deve ser preenchido")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP precisa estar no formato XXXXX-XXX")
    private String zipCode;

    @NotBlank(message = "Bairro não pode ser vazio")
    @Size(max = 128, message = "Bairro deve ter no máximo 128 caracteres")
    private String neighbourhood;

    @NotNull(message = "Número não pode ser vazio")
    private Integer number;

    @NotNull(message = "Cidade não pode ser vazio")
    private Long city;
}
