package com.thpiffer.myfin.app.enumeration;

public enum EnumWalletType {

    CASH("Dinheiro físico"),
    BANK_ACCOUNT("Conta corrente"),
    INVESTMENT_ACCOUNT("Conta de investimentos"),
    VOUCHER("Voucher");

    private final String description;

    EnumWalletType(String description) {
        this.description = description;
    }

}
