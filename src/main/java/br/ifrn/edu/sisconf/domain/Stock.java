package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrepreneur_id", unique = true)
    private Entrepreneur entrepreneur;

    @OneToMany(mappedBy = "stock", targetEntity = StockFood.class)
    private List<StockFood> foods = new ArrayList<>();

    public List<Food> getFoods() {
        return foods.stream()
                .map(StockFood::getFood)
                .collect(Collectors.toList());
    }
}
