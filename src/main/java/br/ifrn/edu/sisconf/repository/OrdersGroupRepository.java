package br.ifrn.edu.sisconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sisconf.domain.OrdersGroup;

@Repository
public interface OrdersGroupRepository extends JpaRepository<OrdersGroup, Long> {

}
