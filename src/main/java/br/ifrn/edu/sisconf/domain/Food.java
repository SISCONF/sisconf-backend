package br.ifrn.edu.sisconf.domain;

import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name="food")
public class Food extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String name;

    @Column(scale = 2, nullable = false)
    private BigDecimal unit_price;

    @Enumerated(EnumType.STRING)
    @Column(length = 7, nullable = false)
    private FoodCategory category;
}
