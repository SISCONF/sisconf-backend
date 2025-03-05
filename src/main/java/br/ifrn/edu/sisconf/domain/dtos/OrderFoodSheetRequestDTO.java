package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.OrderFoodQuantityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFoodSheetRequestDTO {
    private String foodName;
    private Integer quantity;
    private OrderFoodQuantityType quantityType;
}
