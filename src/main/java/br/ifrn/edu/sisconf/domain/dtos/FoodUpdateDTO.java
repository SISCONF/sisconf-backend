package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodUpdateDTO {
    private String name;
    private BigDecimal unitPrice;
    private FoodCategory category;
}
