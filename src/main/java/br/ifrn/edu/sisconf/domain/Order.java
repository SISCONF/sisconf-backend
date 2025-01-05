package br.ifrn.edu.sisconf.domain;

import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(columnDefinition = "UUID", nullable = false)
    private UUID code;

    @Column(name = "total_price", scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "orders_group_id")
    private OrdersGroup ordersGroup;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, targetEntity = OrderFood.class)
    private List<OrderFood> orderFoods = new ArrayList<>();;
}
