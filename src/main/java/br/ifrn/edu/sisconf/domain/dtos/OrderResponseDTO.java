package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private UUID code;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private Long customerId;
    private List<OrderFoodResponseDTO> foods;

}
