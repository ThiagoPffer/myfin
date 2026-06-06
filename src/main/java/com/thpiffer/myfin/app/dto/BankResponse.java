package com.thpiffer.myfin.app.dto;

import java.util.UUID;

public record BankResponse(
    UUID id,
    String name,
    int code
) {}
