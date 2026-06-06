package com.thpiffer.myfin.app.mapper.impl;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.mapper.BankMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankMapperImpl implements BankMapper {

    @Override
    public BankResponse toResponse(BankEntity bank) {
        if (bank == null) {
            return null;
        }

        return new BankResponse(bank.getId(), bank.getName(), bank.getCode());
    }

    @Override
    public BankEntity fromId(UUID id) {
        if (id == null) {
            return null;
        }

        return BankEntity.builder().id(id).build();
    }

}
