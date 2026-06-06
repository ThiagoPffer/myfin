package com.thpiffer.myfin.app.factory.impl;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.factory.WalletFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletFactoryImpl implements WalletFactory {

    @Override
    public WalletEntity create(WalletCreateRequest request) {
        if (request == null) {
            return null;
        }

        var entity = WalletEntity.builder()
                .description(request.description())
                .balance(BigDecimal.ZERO)
                .type(request.type())
                .build();

        if (request.bankId() == null) {
            return entity;
        }

        var bank = BankEntity.builder().id(request.bankId()).build();
        entity.setBank(bank);

        return entity;
    }

}

