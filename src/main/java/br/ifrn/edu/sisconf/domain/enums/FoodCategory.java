package br.ifrn.edu.sisconf.domain.enums;

public enum FoodCategory {
    FRUIT("Fruta"),
    VEGETABLE("Vegetal");

    private String category;

    FoodCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
