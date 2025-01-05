package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByNameAndCategoryAndIdNot(String foodName, FoodCategory category, Long id);
    boolean existsByNameAndCategory(String foodName, FoodCategory category);
}
