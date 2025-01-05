package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;

import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequestDTO {
    private OrderStatus status;

    @Valid
    private List<OrderFoodRequestDTO> foodsQuantities;
}
