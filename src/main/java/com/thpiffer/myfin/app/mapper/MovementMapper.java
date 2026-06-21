package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementResponse;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;

public interface MovementMapper {

    MovementResponse toResponse(MovementEntity entity);

    MovementEntity fromCreateRequest(MovementCreateRequest request, WalletEntity wallet);

    MovementEntity fromUpdateRequest(MovementEntity entity, MovementUpdateRequest request, WalletEntity wallet);

}
