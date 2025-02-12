package br.ifrn.edu.sisconf.domain.enums;

public enum OrderFoodQuantityType {
    KG("KG"),
    UND("UND"),
    CX("CX");

    private String quantityType;

    OrderFoodQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getQuantityType() {
        return quantityType;
    }
}
