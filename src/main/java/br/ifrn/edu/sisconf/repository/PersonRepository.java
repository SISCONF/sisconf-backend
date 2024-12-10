package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
    boolean existsByPhone(String phone);
}
