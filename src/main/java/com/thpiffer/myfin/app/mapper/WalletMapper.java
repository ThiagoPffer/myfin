package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;

public interface WalletMapper {

    WalletResponse toResponse(WalletEntity wallet);

    WalletEntity fromUpdateRequest(WalletEntity entity, WalletUpdateRequest request);

}

