package br.ifrn.edu.sisconf.domain;

import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food")
public class Food extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String name;

    @Column(scale = 2, nullable = false, name = "unit_price")
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 9, nullable = false)
    private FoodCategory category;
}
