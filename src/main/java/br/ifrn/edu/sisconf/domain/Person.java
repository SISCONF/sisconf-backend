package br.ifrn.edu.sisconf.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person extends BaseEntity {
    @Column(length = 128, nullable = false, name = "first_name")
    private String firstName;

    @Column(length = 128, name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private Integer cpf;

    @Column(unique = true)
    private Integer cnpj;

    @Column(length = 16)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(length = 255, nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
