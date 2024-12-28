package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "stock")
public class Stock extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrepreneur_id", unique = true)
    private Entrepreneur entrepreneur;

    @OneToMany(mappedBy = "stock", targetEntity = StockFood.class)
    private List<Food> foods = new ArrayList<>();
}
