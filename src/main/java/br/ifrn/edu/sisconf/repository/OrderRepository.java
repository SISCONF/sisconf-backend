package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCode(UUID code);
    Optional<Order> findByIdAndCustomerPersonKeycloakId(Long id, String keycloakId);
    List<Order> findAllByCustomerId(Long id);
    List<Order> findAllByCustomerPersonKeycloakId(String keycloakId);
    List<Order> findAllByOrderByOrderDateDesc();
    boolean existsByIdAndCustomerPersonKeycloakId(Long id, String keycloakId);
}
