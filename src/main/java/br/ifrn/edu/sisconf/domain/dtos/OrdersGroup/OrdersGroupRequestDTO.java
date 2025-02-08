package br.ifrn.edu.sisconf.domain.dtos.OrdersGroup;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersGroupRequestDTO {
    @Size(min = 1, message = "O grupo deve possuir no mínimo um pedido")
    private List<Long> ordersIds = new ArrayList<>();
}
