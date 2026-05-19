package com.thpiffer.myfin.app.enumeration;

public enum EnumMovementStatus {

    PENDING("Pendente"),
    COMPLETED("Concluído"),
    CANCELED("Cancelado");

    private final String description;

    EnumMovementStatus(String description) {
        this.description = description;
    }

}
