package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequestDTO {
    private OrderStatus status;

    @NotEmpty(message = "As frutas/legumes e suas quantidades devem ser informadas.")
    @Valid
    private List<OrderFoodRequestDTO> foodsQuantities;
}
