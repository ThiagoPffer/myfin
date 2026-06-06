package com.thpiffer.myfin.app.resource.impl;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import com.thpiffer.myfin.app.resource.WalletResource;
import com.thpiffer.myfin.app.service.WalletService;
import com.thpiffer.myfin.core.dto.PagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletResourceImpl implements WalletResource {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<WalletResponse>> getWalletScrollPageByFilter(PagingRequest request) {
        var output = walletService.getWalletScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter());
        return ResponseEntity.ok()
                .header("X-Has-Next", String.valueOf(output.hasNext()))
                .body(output.content().stream().map(walletMapper::toResponse).toList());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable UUID id) {
        var wallet = walletService.getWalletById(id);
        return ResponseEntity.ok(walletMapper.toResponse(wallet));
    }

    @Override
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletCreateRequest request) {
        var wallet = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletMapper.toResponse(wallet));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(@PathVariable UUID id, @RequestBody WalletUpdateRequest request) {
        var wallet = walletService.updateWallet(id, request);
        return ResponseEntity.ok(walletMapper.toResponse(wallet));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable UUID id) {
        walletService.deleteWallet(id);
        return ResponseEntity.noContent().build();
    }

}

