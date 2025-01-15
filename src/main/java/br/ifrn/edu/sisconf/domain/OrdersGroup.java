package br.ifrn.edu.sisconf.domain;

import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders_group")
public class OrdersGroup extends BaseEntity {
    @Column(nullable = false, scale = 2, name = "total_price")
    private BigDecimal totalPrice;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "current_status")
    private OrdersGroupStatus currentStatus;

    @Column(nullable = false, name = "item_quantity")
    private Integer itemQuantity;

    @Column(name = "doc_url", length = 255)
    private String docUrl;

    @OneToMany(mappedBy = "ordersGroup")
    private List<Order> orders = new ArrayList<>();
}