package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface WalletService {

    Optional<WalletEntity> getWalletById(UUID id);

    ScrollingOutput<WalletEntity> getWalletScrollPageByFilter(PageRequest page, String filter);

    WalletEntity createWallet(WalletCreateRequest request);

    WalletEntity updateWallet(UUID id, WalletUpdateRequest request);

    void deleteWallet(UUID id);

}

