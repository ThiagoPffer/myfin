package com.thpiffer.myfin.app.dto;

import com.thpiffer.myfin.app.enumeration.EnumMovementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MovementResponse(
        UUID id,
        int code,
        String description,
        BigDecimal value,
        WalletResponse wallet,
        EnumMovementStatus status,
        LocalDate movementDate
) {
}
