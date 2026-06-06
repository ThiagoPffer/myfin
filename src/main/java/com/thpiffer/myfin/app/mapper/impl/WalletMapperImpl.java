package com.thpiffer.myfin.app.mapper.impl;

import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.mapper.BankMapper;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletMapperImpl implements WalletMapper {

    private final BankMapper bankMapper;

    @Override
    public WalletResponse toResponse(WalletEntity wallet) {
        if (wallet == null) {
            return null;
        }

        return new WalletResponse(
            wallet.getId(),
            wallet.getCode(),
            wallet.getDescription(),
            wallet.getBalance(),
            wallet.getType(),
            bankMapper.toResponse(wallet.getBank())
        );
    }

    @Override
    public WalletEntity fromUpdateRequest(WalletEntity entity, WalletUpdateRequest request) {
        if (request != null) {
            entity.setDescription(request.description());
            entity.setType(request.type());
            entity.setBank(bankMapper.fromId(request.bankId()));
        }

        return entity;
    }

}

