package br.ifrn.edu.sisconf.domain.dtos.Order;

import java.util.ArrayList;

import br.ifrn.edu.sisconf.domain.dtos.OrderFoodSheetRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSheetRequestDTO {
    private String customerName;
    private ArrayList<OrderFoodSheetRequestDTO> details;
}
