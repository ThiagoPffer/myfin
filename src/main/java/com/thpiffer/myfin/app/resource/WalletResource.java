package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.core.dto.PagingRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface WalletResource {

    ResponseEntity<List<WalletResponse>> getWalletScrollPageByFilter(PagingRequest request);

    ResponseEntity<WalletResponse> getWalletById(UUID id);

    ResponseEntity<WalletResponse> createWallet(WalletCreateRequest request);

    ResponseEntity<WalletResponse> updateWallet(UUID id, WalletUpdateRequest request);

    ResponseEntity<Void> deleteWallet(UUID id);

}

