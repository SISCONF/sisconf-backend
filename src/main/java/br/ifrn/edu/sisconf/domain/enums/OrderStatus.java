package br.ifrn.edu.sisconf.domain.enums;

public enum OrderStatus {
    WAITING("Aguardando"),
    ACCEPTED("Aprovado");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
