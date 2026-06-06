package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.entity.BankEntity;

import java.util.UUID;

public interface BankMapper {

    BankResponse toResponse(BankEntity bank);

    BankEntity fromId(UUID id);

}
