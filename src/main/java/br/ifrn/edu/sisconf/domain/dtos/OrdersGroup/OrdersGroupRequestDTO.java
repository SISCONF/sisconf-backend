package br.ifrn.edu.sisconf.domain.dtos.OrdersGroup;
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
public class OrdersGroupRequestDTO {
    private Integer itemQuantity;
    private String docUrl;
    private List<Long> ordersIds = new ArrayList<>();
}
