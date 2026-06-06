package com.thpiffer.myfin.app.service.impl;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.factory.WalletFactory;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import com.thpiffer.myfin.app.repository.WalletRepository;
import com.thpiffer.myfin.app.service.WalletService;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletFactory factory;
    private final WalletMapper mapper;

    @Override
    public WalletEntity getWalletById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ScrollingOutput<WalletEntity> getWalletScrollPageByFilter(PageRequest page, String filter) {
        var slice = repository.findAll(filter, page);
        return new ScrollingOutput<>(slice.hasNext(), slice.getContent());
    }

    @Override
    @Transactional
    public WalletEntity createWallet(WalletCreateRequest request) {
        var entity = factory.create(request);
        return repository.save(entity);
    }

    @Override
    public WalletEntity updateWallet(UUID id, WalletUpdateRequest request) {
        var entity = repository.findById(id).orElseThrow
                (() -> new NotFoundException("Wallet not found with id: " + id));

        return repository.save(mapper.fromUpdateRequest(entity, request));
    }

    @Override
    public void deleteWallet(UUID id) {
        repository.deleteById(id);
    }

}
