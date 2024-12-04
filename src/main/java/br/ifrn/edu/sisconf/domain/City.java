package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="city")
public class City extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name="country_state_id", nullable = false)
    private CountryState countryState;
}
