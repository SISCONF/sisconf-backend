package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
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
    @NotNull(message = "O ID do estoque é obrigatório")
    private Long stockId;

    @NotEmpty(message = "Pelo menos uma comida tem que ser adicionada no estoque")
    private List<StockFoodListRequestDTO> foods;
}
