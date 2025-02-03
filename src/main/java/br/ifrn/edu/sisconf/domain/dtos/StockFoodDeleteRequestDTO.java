package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockFoodDeleteRequestDTO {
    @NotEmpty(message = "Pelo menos uma comida deve ser informada para remoção.")
    List<Long> foodsIds;
}
