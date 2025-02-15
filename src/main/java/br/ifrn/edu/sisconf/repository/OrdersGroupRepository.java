package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.OrdersGroup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersGroupRepository extends JpaRepository<OrdersGroup, Long> {
    List<OrdersGroup> findAllByOrderByOrderDateDesc();

    @Query("SELECT COUNT(o) > 0 FROM OrdersGroup og JOIN og.orders o WHERE o.id IN :ordersIds")
    boolean existsByOrdersId(@Param("ordersIds") Iterable<Long> ordersIds);
}
