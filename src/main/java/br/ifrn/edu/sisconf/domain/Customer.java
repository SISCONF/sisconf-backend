package br.ifrn.edu.sisconf.domain;

import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerCategory category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();
}
