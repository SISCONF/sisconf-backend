package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCode(UUID code);
    Order findByCustomerId(Long id);
}
