package br.ifrn.edu.sisconf.domain.dtos.Order;

import java.util.List;

import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
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
public class OrderRequestDTO {
    @NotEmpty(message = "As frutas/legumes e suas quantidades devem ser informadas.")
    @Valid
    private List<OrderFoodRequestDTO> foodsQuantities;
}
