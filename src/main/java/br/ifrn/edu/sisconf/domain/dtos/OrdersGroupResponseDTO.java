package br.ifrn.edu.sisconf.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersGroupResponseDTO {
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private OrdersGroupStatus currentStatus;
    private String docUrl;
    private List<ListOrdersGroupItemsResponseDTO> listItems;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
