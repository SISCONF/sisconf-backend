package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
}
