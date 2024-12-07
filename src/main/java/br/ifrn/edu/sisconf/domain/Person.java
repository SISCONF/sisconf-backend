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
@Table(name = "person")
public class Person extends BaseEntity {
    @Column(unique = true, nullable = false)
    private Integer cpf;

    @Column(unique = true)
    private Integer cnpj;

    @Column(nullable = false, length = 15)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
