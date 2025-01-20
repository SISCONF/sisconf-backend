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
    @NotEmpty(message = "Um pedido tem que ter pelo menos 1 alimento.")
    @Valid
    private List<OrderFoodRequestDTO> foodsQuantities;
}
