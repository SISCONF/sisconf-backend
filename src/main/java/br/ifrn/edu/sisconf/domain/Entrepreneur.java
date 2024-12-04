package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "entrepreneur")
public class Entrepreneur extends BaseEntity {
    @Column(length = 128, nullable = false, name = "business_name")
    private String businessName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;
}
