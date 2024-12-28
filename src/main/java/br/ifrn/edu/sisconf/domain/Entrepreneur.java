package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entrepreneur")
public class Entrepreneur extends BaseEntity {
    @Column(length = 128, nullable = false, name = "business_name")
    private String businessName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @OneToOne(mappedBy = "entrepreneur")
    private Stock stock;
}
