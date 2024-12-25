package br.ifrn.edu.sisconf.domain;

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
}
