package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockFoodRequestDTO {
    @NotEmpty(message = "Pelo menos uma comida tem que ser adicionada no estoque")
    @Valid
    private List<StockFoodListRequestDTO> foods;
}
