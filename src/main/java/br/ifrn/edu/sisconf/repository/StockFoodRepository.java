package br.ifrn.edu.sisconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifrn.edu.sisconf.domain.StockFood;

public interface StockFoodRepository extends JpaRepository<StockFood, Long> {
}
