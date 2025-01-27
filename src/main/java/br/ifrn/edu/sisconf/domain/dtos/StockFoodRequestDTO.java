package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockFoodRequestDTO {
    @NotNull(message = "O ID da comida é obrigatório")
    private Long foodId;

    @NotNull(message = "O ID do estoque é obrigatório")
    private Long stockId;

    @Min(value = 1, message = "Pelo menos 1 unidade da comida deve ser adicionada")
    private Integer quantity;
}
