package com.thpiffer.myfin.app.dto;

import com.thpiffer.myfin.app.enumeration.EnumWalletType;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
    UUID id,
    int code,
    String description,
    BigDecimal balance,
    EnumWalletType type,
    BankResponse bank
) {}

