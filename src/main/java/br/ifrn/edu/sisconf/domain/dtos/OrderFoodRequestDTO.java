package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.OrderFoodQuantityType;
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
public class OrderFoodRequestDTO {
    @NotNull(message = "O id da comida não pode ser nulo")
    private Long foodId;

    @NotNull(message = "A quantidade não deve ser nula.")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
    private Integer quantity;

    @NotNull(message = "O tipo da quantidade deve ser informado")
    private OrderFoodQuantityType quantityType;
}
