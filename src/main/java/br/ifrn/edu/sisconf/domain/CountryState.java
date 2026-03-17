package br.ifrn.edu.sisconf.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="country_state")
public class CountryState extends BaseEntity {
    @Column(length=255, nullable = false)
    private String fullname;

    @Column(length=2, unique = true, nullable = false)
    private String abbreviation;

    @OneToMany(mappedBy = "countryState")
    private List<City> cities = new ArrayList<>();
}
