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
public class StockFoodListRequestDTO {
    @NotNull(message = "O ID da comida não pode ser nulo")
    private Long foodId;

    @Min(value = 1, message = "Pelo menos uma unidade da comida tem que ser adicionada")
    private Integer quantity;
}
