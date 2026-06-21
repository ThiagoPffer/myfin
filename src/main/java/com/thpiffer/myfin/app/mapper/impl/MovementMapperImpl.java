package com.thpiffer.myfin.app.mapper.impl;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementResponse;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumMovementStatus;
import com.thpiffer.myfin.app.mapper.MovementMapper;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovementMapperImpl implements MovementMapper {

    private final WalletMapper walletMapper;

    @Override
    public MovementResponse toResponse(MovementEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MovementResponse(
                entity.getId(),
                entity.getCode(),
                entity.getDescription(),
                entity.getValue(),
                walletMapper.toResponse(entity.getWallet()),
                entity.getStatus(),
                entity.getMovementDate()
        );
    }

    @Override
    public MovementEntity fromCreateRequest(MovementCreateRequest request, WalletEntity wallet) {
        if (request == null) {
            return null;
        }

        return MovementEntity.builder()
                .description(request.description())
                .value(request.value())
                .wallet(wallet)
                .movementDate(request.movementDate())
                .status(EnumMovementStatus.PENDING)
                .build();
    }

    @Override
    public MovementEntity fromUpdateRequest(MovementEntity entity, MovementUpdateRequest request, WalletEntity wallet) {
        if (request != null) {
            entity.setDescription(request.description());
            entity.setValue(request.value());
            entity.setWallet(wallet);
            entity.setMovementDate(request.movementDate());
        }

        return entity;
    }

}
