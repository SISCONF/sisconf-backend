package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="city")
public class City extends BaseEntity {
    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="country_state_id", nullable = false)
    private CountryState countryState;
}
