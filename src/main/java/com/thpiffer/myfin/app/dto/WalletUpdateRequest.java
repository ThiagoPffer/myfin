package com.thpiffer.myfin.app.dto;

import com.thpiffer.myfin.app.enumeration.EnumWalletType;

import java.util.UUID;

public record WalletUpdateRequest(
    String description,
    EnumWalletType type,
    UUID bankId
) {}
