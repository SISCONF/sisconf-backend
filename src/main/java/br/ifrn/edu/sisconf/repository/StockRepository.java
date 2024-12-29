package br.ifrn.edu.sisconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifrn.edu.sisconf.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
