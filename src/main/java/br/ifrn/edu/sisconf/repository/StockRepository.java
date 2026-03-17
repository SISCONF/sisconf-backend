package br.ifrn.edu.sisconf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifrn.edu.sisconf.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByEntrepreneurId(Long id);
}
