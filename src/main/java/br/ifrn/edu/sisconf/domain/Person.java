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
    @Column(unique = true, nullable = false, name = "keycloak_id")
    private String keycloakId;

    @Column(nullable = false, length = 128, name = "first_name")
    private String firstName;

    @Column(nullable = false, length = 128, name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false, length = 128)
    private String email;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(unique = true, length = 15)
    private String cnpj;

    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
