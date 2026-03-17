package br.ifrn.edu.sisconf.domain.enums;

public enum OrdersGroupStatus {
    PLACED("Fechado"),
    RECEIVED("Recebido"),
    DELIVERED("Entregue");

    private String status;

    OrdersGroupStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
