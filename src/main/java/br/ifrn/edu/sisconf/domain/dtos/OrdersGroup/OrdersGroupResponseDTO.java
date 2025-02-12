package br.ifrn.edu.sisconf.domain.dtos.OrdersGroup;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersGroupResponseDTO {
    private Long id;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private OrdersGroupStatus currentStatus;
    private Integer itemQuantity;
    private String docUrl;
    private List<OrderResponseDTO> orders = new ArrayList<>();
}
