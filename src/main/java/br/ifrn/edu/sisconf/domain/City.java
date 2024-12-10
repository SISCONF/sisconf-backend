package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="city")
public class City extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="country_state_id", nullable = false)
    private CountryState countryState;
}
