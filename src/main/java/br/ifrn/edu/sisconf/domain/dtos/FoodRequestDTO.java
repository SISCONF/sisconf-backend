package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, min = 1, message = "O nome da comida deve ter entre 1 e 255 caracteres")
    private String name;

    @NotNull(message = "O preço unitário é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço da comida deve ser maior do que 0")
    private BigDecimal unitPrice;

    @NotNull(message = "A categoria da comida é obrigatória")
    private FoodCategory category;
}
