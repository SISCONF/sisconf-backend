package br.ifrn.edu.sisconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sisconf.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
