package br.ifrn.edu.sisconf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifrn.edu.sisconf.domain.StockFood;

public interface StockFoodRepository extends JpaRepository<StockFood, Long> {
    Optional<StockFood> findByStockIdAndFoodId(Long stockId, Long foodId);
    List<StockFood> findByStockIdAndFoodIdIn(Long stockId, List<Long> foodsIds);
}


