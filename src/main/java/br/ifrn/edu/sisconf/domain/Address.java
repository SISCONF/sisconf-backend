package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="address")
public class Address extends BaseEntity {
    @Column(length = 128, nullable = false)
    private String street;
    @Column(nullable = false)
    private Integer zip_code;
    @Column(length = 128, nullable = false)
    private String neighbourhood;
    @Column(nullable = false)
    private Integer number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
