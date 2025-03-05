package br.ifrn.edu.sisconf.specification;

import org.springframework.data.jpa.domain.Specification;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;

public class FoodSpecification {
    public static Specification<Food> ofFoodCategory(FoodCategory foodCategory) {
        return (root, query, cb) ->
            foodCategory == null ? null : cb.equal(root.get("category"), foodCategory);
    }
}
