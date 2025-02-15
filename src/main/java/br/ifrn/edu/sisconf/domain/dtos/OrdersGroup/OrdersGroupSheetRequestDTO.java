package br.ifrn.edu.sisconf.domain.dtos.OrdersGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import br.ifrn.edu.sisconf.domain.dtos.Order.OrderSheetRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersGroupSheetRequestDTO {
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private Integer itemQuantity;
    private ArrayList<OrderSheetRequestDTO> orders;
}
