package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String name;

    @NotNull(message = "O preço unitário é obrigatório")
    private BigDecimal unitPrice;

    @NotNull(message = "A categoria da comida é obrigatória")
    private FoodCategory category;
}
