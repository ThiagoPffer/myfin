package com.thpiffer.myfin.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MovementCreateRequest(
        String description,
        BigDecimal value,
        UUID walletId,
        LocalDate movementDate
) {
}
