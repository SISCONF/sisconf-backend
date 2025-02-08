package br.ifrn.edu.sisconf.domain.dtos.OrdersGroup;
import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersGroupRequestDTO {
    private OrdersGroupStatus currentStatus;
    private List<Long> ordersIds = new ArrayList<>();
}
