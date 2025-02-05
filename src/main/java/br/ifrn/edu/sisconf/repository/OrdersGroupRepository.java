package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.OrdersGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersGroupRepository extends JpaRepository<OrdersGroup, Long> {
}
