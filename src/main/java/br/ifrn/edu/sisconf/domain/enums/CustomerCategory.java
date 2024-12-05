package br.ifrn.edu.sisconf.domain.enums;

public enum CustomerCategory {
    MARKETER("Feirante"),
    ENTREPRENEUR("Empreendedor"),
    OTHER("Outros");

    private String category;

    CustomerCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

}
